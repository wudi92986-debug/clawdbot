package com.petfuneral.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petfuneral.common.response.PageResult;
import com.petfuneral.common.response.Result;
import com.petfuneral.dto.PetCreateRequest;
import com.petfuneral.dto.PetDeathRequest;
import com.petfuneral.entity.Pet;
import com.petfuneral.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物管理控制器
 */
@Tag(name = "宠物管理")
@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @Operation(summary = "分页查询宠物")
    @GetMapping
    public Result<PageResult<Pet>> getPetPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long customerId) {
        IPage<Pet> result = petService.getPetPage(page, size, keyword, status, customerId);
        return Result.success(PageResult.of(result));
    }

    @Operation(summary = "获取宠物详情")
    @GetMapping("/{id}")
    public Result<Pet> getPetDetail(@PathVariable Long id) {
        return Result.success(petService.getPetDetail(id));
    }

    @Operation(summary = "获取客户的所有宠物")
    @GetMapping("/customer/{customerId}")
    public Result<List<Pet>> getByCustomerId(@PathVariable Long customerId) {
        return Result.success(petService.getByCustomerId(customerId));
    }

    @Operation(summary = "创建宠物")
    @PostMapping
    public Result<Pet> createPet(@Valid @RequestBody PetCreateRequest request) {
        Pet pet = petService.createPet(request);
        return Result.success("创建成功", pet);
    }

    @Operation(summary = "更新宠物信息")
    @PutMapping("/{id}")
    public Result<Void> updatePet(@PathVariable Long id, @Valid @RequestBody PetCreateRequest request) {
        petService.updatePet(id, request);
        return Result.success("更新成功");
    }

    @Operation(summary = "登记宠物离世")
    @PostMapping("/{id}/death")
    public Result<Void> registerDeath(@PathVariable Long id, @Valid @RequestBody PetDeathRequest request) {
        petService.registerDeath(id, request);
        return Result.success("登记成功");
    }

    @Operation(summary = "删除宠物")
    @DeleteMapping("/{id}")
    public Result<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "统计宠物数量")
    @GetMapping("/count")
    public Result<Long> countPets(@RequestParam(required = false) Integer status) {
        if (status != null) {
            return Result.success(petService.countByStatus(status));
        }
        return Result.success(petService.count());
    }
}
