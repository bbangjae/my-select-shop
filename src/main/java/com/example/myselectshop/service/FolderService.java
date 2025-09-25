package com.example.myselectshop.service;

import com.example.myselectshop.dto.FolderResponseDto;
import com.example.myselectshop.entity.Folder;
import com.example.myselectshop.entity.User;
import com.example.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {
        Set<String> existFolderNames = folderRepository.findAllByUserAndNameIn(user, folderNames).stream()
                .map(Folder::getName)
                .collect(Collectors.toSet());

        List<Folder> foldersToCreate = folderNames.stream()
                .map(folderName -> {
                    if (existFolderNames.contains(folderName)) {
                        throw new IllegalArgumentException("폴더명이 중복 되었습니다.");
                    }
                    return new Folder(folderName, user);
                })
                .toList();

        folderRepository.saveAll(foldersToCreate);
    }

    public List<FolderResponseDto> getFolders(User user) {
        return folderRepository.findAllByUser(user).stream()
                .map(FolderResponseDto::of)
                .toList();
    }
}
