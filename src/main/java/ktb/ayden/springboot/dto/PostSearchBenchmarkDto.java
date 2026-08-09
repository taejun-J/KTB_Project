package ktb.ayden.springboot.dto;

public record PostSearchBenchmarkDto(
        Long postId,
        String postName,
        String status
) {
}