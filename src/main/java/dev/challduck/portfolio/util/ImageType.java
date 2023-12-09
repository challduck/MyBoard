package dev.challduck.portfolio.util;

public enum ImageType {
    JPEG(".jpeg", "image/jpeg"),
    JPG(".jpg", "image/jpeg"),
    PNG(".png", "image/png"),
    GIF(".gif", "image/gif"),
    WEBP(".webp", "image/webp");

    private final String extension;
    private final String contentType;

    ImageType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public static ImageType findByContentType(String contentType) {
        for (ImageType imageType : values()) {
            if (imageType.getContentType().equals(contentType)) {
                return imageType;
            }
        }
        return null; // 해당하는 확장자가 없으면 null 반환 또는 예외 처리 추가
    }
}
