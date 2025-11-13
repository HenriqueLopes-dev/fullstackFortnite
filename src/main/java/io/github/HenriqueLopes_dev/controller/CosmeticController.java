package io.github.HenriqueLopes_dev.controller;

import io.github.HenriqueLopes_dev.dto.cosmeticBundle.ShopBundleViewDTO;
import io.github.HenriqueLopes_dev.mapper.CosmeticMapper;
import io.github.HenriqueLopes_dev.model.Cosmetic;
import io.github.HenriqueLopes_dev.service.CosmeticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("cosmetics")
@RequiredArgsConstructor
public class CosmeticController implements GenericController{

    private final CosmeticService service;
    private final CosmeticMapper mapper;

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "rarity", required = false) String rarity,
            @RequestParam(value = "inclusion-date", required = false) LocalDate inclusionDate,
            @RequestParam(value = "new", required = false) Boolean isNew,
            @RequestParam(value = "shop", required = false) Boolean onShop,
            @RequestParam(value = "sale", required = false) Boolean onSale,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "page-size", defaultValue = "30") Integer pageSize
    ){
        if (Boolean.TRUE.equals(onShop)) {
            Page<ShopBundleViewDTO> shopPage = service.getShopBundlesView(
                    name, type, rarity, inclusionDate, isNew, onSale, page, pageSize
            );
            return ResponseEntity.ok(shopPage);
        }

        // comportamento antigo (cosmetics paginados)
        Page<Cosmetic> pageResult = service.search(
                name, type, rarity, inclusionDate, isNew, onShop, onSale, page, pageSize
        );
        Page<Object> result = pageResult.map(mapper::toDTO);
        return ResponseEntity.ok(result);

    }

}
