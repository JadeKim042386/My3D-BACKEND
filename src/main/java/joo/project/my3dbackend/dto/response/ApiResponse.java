package joo.project.my3dbackend.dto.response;

public record ApiResponse(Object data) {
    public static ApiResponse of(Object data) {
        return new ApiResponse(data);
    }
}
