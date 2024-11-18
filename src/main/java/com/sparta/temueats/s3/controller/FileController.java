package com.sparta.temueats.s3.controller;

import com.sparta.temueats.global.ResponseDto;
import com.sparta.temueats.s3.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.sparta.temueats.global.ResponseDto.SUCCESS;

@Tag(name="s3 파일 생성")
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @Operation(summary = "s3 파일 업로드")
    @PostMapping
    public ResponseDto<String> uploadFile(MultipartFile file) throws IOException {
        return new ResponseDto<>(SUCCESS, "파일 업로드 성공", fileService.uploadFile(file));
    }

}
