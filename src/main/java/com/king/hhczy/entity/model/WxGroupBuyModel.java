package com.king.hhczy.entity.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author ningjinxiang
 * @since 2019-10-17
 */
@Data
public class WxGroupBuyModel {
    @NotNull(message ="creatorId不能为空")
    private Integer creatorId;

    @NotBlank(message = "title不能为空")
    private String title;

    private Integer lastEditorId;
    @NotBlank(message = "contents不能为空")
    private String contents;
}
