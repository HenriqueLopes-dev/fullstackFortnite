package io.github.HenriqueLopes_dev.service;

import io.github.HenriqueLopes_dev.dto.cosmetic.CosmeticDTO;
import io.github.HenriqueLopes_dev.dto.cosmeticBundle.ShopBundleViewDTO;
import io.github.HenriqueLopes_dev.mapper.CosmeticMapper;
import io.github.HenriqueLopes_dev.model.Cosmetic;
import io.github.HenriqueLopes_dev.model.CosmeticBundle;
import io.github.HenriqueLopes_dev.repository.CosmeticRepository;
import io.github.HenriqueLopes_dev.repository.specs.CosmeticSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.HenriqueLopes_dev.repository.specs.CosmeticSpecs.*;

@Service
@RequiredArgsConstructor
public class CosmeticService {

    private final CosmeticRepository repository;
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

        if (name != null){
            specs = specs.and(nameLike(name, onShop));
        }

        if (type != null){
            specs = specs.and(typeLike(type));
        }

        if (rarity != null){
            specs = specs.and(rarityLike(rarity));
        }

        if (inclusionDate != null){
            specs = specs.and(inclusionDateEqual(inclusionDate));
        }

        if (isNew != null){
            specs = specs.and(isNewEqual(isNew));
        }

        if (onShop != null){
            specs = specs.and(onShopEqual(onShop));
        }

        if (onSale != null){
            specs = specs.and(onSaleEqual(onSale));
        }

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

        if (name != null) spec = spec.and(CosmeticSpecs.nameLike(name, true));
        if (type != null) spec = spec.and(CosmeticSpecs.typeLike(type));
        if (rarity != null) spec = spec.and(CosmeticSpecs.rarityLike(rarity));
        if (inclusionDate != null) spec = spec.and(CosmeticSpecs.inclusionDateEqual(inclusionDate));
        if (isNew != null) spec = spec.and(CosmeticSpecs.isNewEqual(isNew));
        if (onSale != null) spec = spec.and(CosmeticSpecs.onSaleEqual(onSale));

        spec = spec.and(CosmeticSpecs.onShopEqual(true));

        List<Cosmetic> allMatching = repository.findAll(spec);

        // 3) Agrupa por bundle (bundle != null) ou por "solo" (bundle null)
        // Key strategy: se bundle != null -> agrupar por bundle.getId().toString(); senão -> agrupar por "SOLO_" + externalId
        Map<String, List<Cosmetic>> grouped = allMatching.stream()
                .collect(Collectors.groupingBy(c -> {
                    if (c.getBundle() != null) return "BUNDLE_" + c.getBundle().getId().toString();
                    return "SOLO_" + c.getExternalId();
                }));

        // 4) Constrói a lista de ShopBundleViewDTO
        List<ShopBundleViewDTO> shopList = new ArrayList<>(grouped.size());

        for (Map.Entry<String, List<Cosmetic>> e : grouped.entrySet()) {
            List<Cosmetic> group = e.getValue();

            // se tem bundle (todos terão a mesma bundle)
            Cosmetic c = group.get(0);
            if (c.getBundle() != null) {
                CosmeticBundle b = c.getBundle();
                List<CosmeticDTO> cosmeticsDto = group.stream().map(mapper::toDTO).toList();

                // usar preço do bundle se existir, senão podemos derivar de itens
                Integer regular = b.getRegularPrice();
                Integer finalP = b.getFinalPrice();

                shopList.add(new ShopBundleViewDTO(
                        b.getId().toString(),
                        b.getName(),
                        b.getImageUrl(),
                        regular,
                        finalP,
                        cosmeticsDto
                ));
            }
        }

        // 5) Paginação manual do resultado de bundles
        int start = page * pageSize;
        int end = Math.min(start + pageSize, shopList.size());
        List<ShopBundleViewDTO> pageContent = (start <= end) ? shopList.subList(start, end) : List.of();

        Pageable pageable = PageRequest.of(page, pageSize);
        return new PageImpl<>(pageContent, pageable, shopList.size());
    }

}
