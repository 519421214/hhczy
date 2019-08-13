/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.king.hhczy.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collections;
import java.util.List;

/**
 * 从com.baomidou.mybatisplus.extension.plugins.pagination.Page改造而来，主要增加了@ApiModel等注解配合swagger从而自动生成在线文档
 *
 * @author hubin
 * @since 2018-06-09
 */
@ApiModel
public class MyPage<T> implements IPage<T> {

    private static final long serialVersionUID = 8545996863226528798L;
    /**
     * 为了文档展示用，无实际操作赋值
     */
    @ApiModelProperty("总页数")
    private long pages = 0;
    /**
     * 查询数据列表
     */
    @ApiModelProperty("记录列表")
    private List<T> records = Collections.emptyList();
    /**
     * 总数
     */
    @ApiModelProperty("总记录数")
    private long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    @ApiModelProperty("每页数")
    private long size = 10;
    /**
     * 当前页
     */
    @ApiModelProperty("当前页码，从1开始")
    private long current = 1;
    /**
     * <p>
     * SQL 排序 ASC 数组
     * </p>
     */
    private String[] ascs;
    /**
     * <p>
     * SQL 排序 DESC 数组
     * </p>
     */
    private String[] descs;
    /**
     * <p>
     * 自动优化 COUNT SQL
     * </p>
     */
    private boolean optimizeCountSql = true;
    /**
     * <p>
     * 是否进行 count 查询
     * </p>
     */
    private boolean isSearchCount = true;

    public MyPage() {
        // to do nothing
    }

    /**
     * <p>
     * 分页构造函数
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public MyPage(long current, long size) {
        this(current, size, 0);
    }

    public MyPage(long current, long size, long total) {
        this(current, size, total, true);
    }

    public MyPage(long current, long size, boolean isSearchCount) {
        this(current, size, 0, isSearchCount);
    }

    public MyPage(long current, long size, long total, boolean isSearchCount) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        this.isSearchCount = isSearchCount;
    }

    /**
     * <p>
     * 是否存在上一页
     * </p>
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * <p>
     * 是否存在下一页
     * </p>
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public MyPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public MyPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public MyPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public MyPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    @Override
    public String[] ascs() {
        return ascs;
    }

    public MyPage<T> setAscs(List<String> ascs) {
        if (CollectionUtils.isNotEmpty(ascs)) {
            this.ascs = ascs.toArray(new String[0]);
        }
        return this;
    }


    /**
     * <p>
     * 升序
     * </p>
     *
     * @param ascs 多个升序字段
     */
    public MyPage<T> setAsc(String... ascs) {
        this.ascs = ascs;
        return this;
    }

    @Override
    public String[] descs() {
        return descs;
    }

    public MyPage<T> setDescs(List<String> descs) {
        if (CollectionUtils.isNotEmpty(descs)) {
            this.descs = descs.toArray(new String[0]);
        }
        return this;
    }

    /**
     * <p>
     * 降序
     * </p>
     *
     * @param descs 多个降序字段
     */
    public MyPage<T> setDesc(String... descs) {
        this.descs = descs;
        return this;
    }

    @Override
    public boolean optimizeCountSql() {
        return optimizeCountSql;
    }

    @Override
    public boolean isSearchCount() {
        if (total < 0) {
            return false;
        }
        return isSearchCount;
    }

    MyPage<T> setSearchCount(boolean isSearchCount) {
        this.isSearchCount = isSearchCount;
        return this;
    }

    public MyPage<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }
}
