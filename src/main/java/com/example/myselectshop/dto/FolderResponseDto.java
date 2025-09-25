package com.example.myselectshop.dto;

import com.example.myselectshop.entity.Folder;
import lombok.Getter;

@Getter
public class FolderResponseDto {
    private Long id;
    private String name;

    public FolderResponseDto(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
    }

    public static FolderResponseDto of(Folder folder) {
        return new FolderResponseDto(folder);
    }
}