package com.king.hhczy.entity.model;

import lombok.Data;

@Data
public class UploadModel {
    private String filePath;
    private String fileName;
    private byte[] fileBuf;
}
