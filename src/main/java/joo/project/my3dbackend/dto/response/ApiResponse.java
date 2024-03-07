package joo.project.my3dbackend.dto.response;

public record ApiResponse(String data) {
    public static ApiResponse of(String data) {
        return new ApiResponse(data);
    }
}
