package com.ds.seckill.util.dto;

public class DTOUtil {

    public static <T> DTO newInstance(boolean isSuccess, String statusCode, String message, T data){
        return new DTO(isSuccess, statusCode, message, data);
    }

    public static DTO newInvalidUserDTO(String statusCode){
        return newInstance(false, statusCode, "Invalid user.", null);
    }

    public static DTO newNotLoggedInDTO(String statusCode){
        return newInstance(false, statusCode, "Not logged in", null);
    }

    public static DTO newMismatchedRoleDTO(String statusCode){
        return newInstance(false, statusCode, "Mismatched role", null);
    }

    public static DTO newUnauthorizedAccessDTO(String statusCode){
        return newInstance(false, statusCode, "Unauthorized access", null);
    }

}
