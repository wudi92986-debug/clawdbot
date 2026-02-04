package com.zhanjiang.photography.common.result;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果
 */
@Data
public class PageResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<T> list;
    private Pagination pagination;
    
    @Data
    public static class Pagination implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer page;
        private Integer pageSize;
        private Long total;
        private Integer totalPages;
    }
    
    public static <T> PageResult<T> of(List<T> list, long page, long pageSize, long total) {
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        
        Pagination pagination = new Pagination();
        pagination.setPage((int) page);
        pagination.setPageSize((int) pageSize);
        pagination.setTotal(total);
        pagination.setTotalPages((int) Math.ceil((double) total / pageSize));
        result.setPagination(pagination);
        
        return result;
    }
}
