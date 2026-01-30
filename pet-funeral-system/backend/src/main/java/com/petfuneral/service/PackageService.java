package com.petfuneral.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petfuneral.common.exception.BusinessException;
import com.petfuneral.entity.ServicePackage;
import com.petfuneral.mapper.ServicePackageMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 服务套餐服务
 */
@Service
public class PackageService extends ServiceImpl<ServicePackageMapper, ServicePackage> {

    /**
     * 分页获取套餐列表
     */
    public IPage<ServicePackage> getPackagePage(Integer page, Integer pageSize, String keyword) {
        LambdaQueryWrapper<ServicePackage> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(ServicePackage::getName, keyword);
        }
        wrapper.orderByAsc(ServicePackage::getSortOrder);
        return this.page(new Page<>(page, pageSize), wrapper);
    }

    /**
     * 获取所有上架套餐
     */
    public List<ServicePackage> getActivePackages() {
        return this.list(
                new LambdaQueryWrapper<ServicePackage>()
                        .eq(ServicePackage::getStatus, 1)
                        .orderByAsc(ServicePackage::getSortOrder)
        );
    }

    /**
     * 获取所有套餐 (管理端)
     */
    public List<ServicePackage> getAllPackages() {
        return this.list(
                new LambdaQueryWrapper<ServicePackage>()
                        .orderByAsc(ServicePackage::getSortOrder)
        );
    }

    /**
     * 获取套餐详情
     */
    public ServicePackage getPackageDetail(Long packageId) {
        ServicePackage servicePackage = this.getById(packageId);
        if (servicePackage == null) {
            throw new BusinessException("套餐不存在");
        }
        return servicePackage;
    }

    /**
     * 创建套餐
     */
    public ServicePackage createPackage(ServicePackage servicePackage) {
        this.save(servicePackage);
        return servicePackage;
    }

    /**
     * 更新套餐
     */
    public void updatePackage(ServicePackage servicePackage) {
        this.updateById(servicePackage);
    }

    /**
     * 上架/下架套餐
     */
    public void toggleStatus(Long packageId) {
        ServicePackage servicePackage = getPackageDetail(packageId);
        servicePackage.setStatus(servicePackage.getStatus() == 1 ? 0 : 1);
        this.updateById(servicePackage);
    }
}
