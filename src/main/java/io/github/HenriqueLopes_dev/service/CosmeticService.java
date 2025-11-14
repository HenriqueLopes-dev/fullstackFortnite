package io.github.HenriqueLopes_dev.service;

import io.github.HenriqueLopes_dev.dto.cosmeticBundle.ShopBundleViewDTO;
import io.github.HenriqueLopes_dev.exception.NotEnoughTokensException;
import io.github.HenriqueLopes_dev.mapper.CosmeticMapper;
import io.github.HenriqueLopes_dev.model.*;
import io.github.HenriqueLopes_dev.repository.CosmeticBundleRepository;
import io.github.HenriqueLopes_dev.repository.CosmeticRepository;
import io.github.HenriqueLopes_dev.repository.PurchaseHistoryRepository;
import io.github.HenriqueLopes_dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static io.github.HenriqueLopes_dev.repository.specs.CosmeticSpecs.*;

@Service
@RequiredArgsConstructor
public class CosmeticService {

    private final CosmeticRepository repository;
    private final CosmeticBundleRepository bundleRepository;
    private final UserRepository userRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final CosmeticMapper mapper;

    public Page<Cosmetic> search(
            String name,
            String type,
            String rarity,
            LocalDate inclusionDate,
            Boolean isNew,
            Boolean onShop,
            Boolean onSale,
            Integer page,
            Integer pageSize
    ) {

        Specification<Cosmetic> specs = Specification.where(null);

        if (name != null)
            specs = specs.and(nameLike(name, onShop));

        if (type != null)
            specs = specs.and(typeLike(type));

        if (rarity != null)
            specs = specs.and(rarityLike(rarity));

        if (inclusionDate != null)
            specs = specs.and(inclusionDateEqual(inclusionDate));

        if (isNew != null)
            specs = specs.and(isNewEqual(isNew));

        if (onShop != null)
            specs = specs.and(onShopEqual(onShop));

        if (onSale != null)
            specs = specs.and(onSaleEqual(onSale));

        Pageable pageRequest = PageRequest.of(page, pageSize);

        return repository.findAll(specs, pageRequest);
    }

    public Page<ShopBundleViewDTO> getShopBundlesView(
            String name,
            String type,
            String rarity,
            LocalDate inclusionDate,
            Boolean isNew,
            Boolean onSale,
            int page,
            int pageSize
    ) {
        Specification<Cosmetic> spec = Specification.where(null);

        if (name != null) spec = spec.and(nameLike(name, true));
        if (type != null) spec = spec.and(typeLike(type));
        if (rarity != null) spec = spec.and(rarityLike(rarity));
        if (inclusionDate != null) spec = spec.and(inclusionDateEqual(inclusionDate));
        if (isNew != null) spec = spec.and(isNewEqual(isNew));
        if (onSale != null) spec = spec.and(onSaleEqual(onSale));

        spec = spec.and(onShopEqual(true));
        List<Cosmetic> allMatching = repository.findAll(spec);

        allMatching = allMatching.stream().distinct().toList();

        Map<String, ShopBundleViewDTO> shopMap = new LinkedHashMap<>();

        for (Cosmetic cosmetic : allMatching) {

            if (cosmetic.getBundles() != null && !cosmetic.getBundles().isEmpty()) {

                for (CosmeticBundleRelation link : cosmetic.getBundles()) {
                    CosmeticBundle bundle = link.getBundle();

                    if (onSale != null) {
                        if (onSale.booleanValue() != bundle.getIsOnSale()) {
                            continue;
                        }
                    }

                    ShopBundleViewDTO view = shopMap.computeIfAbsent(
                            "BUNDLE_" + bundle.getId(),
                            k -> new ShopBundleViewDTO(
                                    bundle.getId().toString(),
                                    bundle.getName(),
                                    bundle.getImageUrl(),
                                    bundle.getRegularPrice(),
                                    bundle.getFinalPrice(),
                                    new ArrayList<>()
                            )
                    );

                    view.cosmetics().add(mapper.toDTO(cosmetic));
                }

            } else {
                ShopBundleViewDTO solo = new ShopBundleViewDTO(
                        "SOLO_" + cosmetic.getExternalId(),
                        cosmetic.getName(),
                        cosmetic.getImageUrl(),
                        null,
                        null,
                        new ArrayList<>()
                );
                solo.cosmetics().add(mapper.toDTO(cosmetic));

                if (onSale != null) {
                    boolean isSoloOnSale = solo.regularPrice() != null && solo.finalPrice() != null && solo.finalPrice() < solo.regularPrice();
                    if (onSale != isSoloOnSale) {
                        continue;
                    }
                }

                shopMap.put(solo.bundleId(), solo);
            }
        }

        List<ShopBundleViewDTO> shopList = new ArrayList<>(shopMap.values());

        int start = page * pageSize;
        int end = Math.min(start + pageSize, shopList.size());
        List<ShopBundleViewDTO> pageContent = (start < end)
                ? shopList.subList(start, end)
                : List.of();

        Pageable pageable = PageRequest.of(page, pageSize);
        return new PageImpl<>(pageContent, pageable, shopList.size());
    }

    public Optional<Cosmetic> findByExternalId(String externalId) {
        return repository.findByExternalId(externalId);
    }

    public Optional<CosmeticBundle> purchaseBundleById(UUID bundleId) {
        Optional<CosmeticBundle> opBundle = bundleRepository.findById(bundleId);

        if (opBundle.isEmpty()){
            return opBundle;
        }
        CosmeticBundle bundle = opBundle.get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Userr user = (Userr) auth.getPrincipal();

        int balance = user.getBalance();
        Integer bundlePrice = bundle.getFinalPrice();

        if (balance < bundlePrice){
            throw new NotEnoughTokensException("Você não possui tokens suficientes para comprar este produto!");
        }

        PurchaseHistory pHistory = new PurchaseHistory();

        pHistory.setPrice(bundle.getFinalPrice());
        pHistory.setBundleImage(bundle.getImageUrl());
        pHistory.setBundleName(bundle.getName());
        pHistory.setCosmetics(
                bundle.getCosmetics().stream()
                        .map(CosmeticBundleRelation::getCosmetic).toList()
        );

        user.getPurchaseHistory().add(pHistory);

        user.setBalance(balance - bundlePrice);
        userRepository.save(user);
        purchaseHistoryRepository.save(pHistory);


        return opBundle;
    }

    public Optional<CosmeticBundle> findBubleById(UUID bundleId) {
        return bundleRepository.findByIdWithCosmetics(bundleId);
    }
}
