package cc.worldmandia.url.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUrlResponse {

    private Error error;
    private Long createUrlId;

    public enum Error {
        ok,
        invalidTitle,
        invalidDescription
    }

    public static CreateUrlResponse success(Long createUrlId){
        return builder().error(Error.ok).createUrlId(createUrlId).build();
    }

    public static CreateUrlResponse failed(Error error){
        return builder().error(error).createUrlId(-1L).build();
    }
}