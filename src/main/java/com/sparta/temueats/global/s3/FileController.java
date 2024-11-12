package com.sparta.temueats.global.s3;

import com.sparta.temueats.global.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.sparta.temueats.global.ResponseDto.SUCCESS;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseDto<String> uploadFile(MultipartFile file) throws IOException {
        return new ResponseDto<>(SUCCESS, "파일 업로드 성공", fileService.uploadFile(file));
    }

}
