package com.mousecall.mousecall.attachment.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import javax.imageio.ImageIO;

@Service
public class FileService {

    // 파일 저장경로를 상대위치 uploads로 설정
    // 그아래 오리지날,썸네일, 워터마크 폴더로 관리
    private final Path basePath = Paths.get("uploads");

    public FileService() throws IOException {
        Files.createDirectories(basePath.resolve("original"));
        Files.createDirectories(basePath.resolve("thumbnail"));
        Files.createDirectories(basePath.resolve("watermark"));
    }

    public String saveOriginalFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetPath = basePath.resolve("original").resolve(filename);
        file.transferTo(targetPath);
        return targetPath.toString();
    }

    public String generateThumbnail(String originalPath) throws IOException {
        String filename = Paths.get(originalPath).getFileName().toString();
        Path thumbnailPath = basePath.resolve("thumbnail").resolve(filename);

        Thumbnails.of(new File(originalPath))
                .size(150, 150)
                .toFile(thumbnailPath.toFile());

        return thumbnailPath.toString();
    }

    public String applyWatermark(String originalPath) throws IOException {
        String filename = Paths.get(originalPath).getFileName().toString();
        Path watermarkPath = basePath.resolve("watermark").resolve(filename);

        BufferedImage originalImage = ImageIO.read(new File(originalPath));
        Graphics2D g2d = originalImage.createGraphics();

        String watermarkText = "MouseCall";
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        g2d.setComposite(alpha);
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        g2d.drawString(watermarkText, 10, originalImage.getHeight() - 10);
        g2d.dispose();

        ImageIO.write(originalImage, "png", watermarkPath.toFile());

        return watermarkPath.toString();
    }
}
