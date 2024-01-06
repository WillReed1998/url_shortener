package cc.worldmandia.url.response;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class EnableUrlResponse {
    private Error error;
    private Long id;
    private boolean enable;

    public enum Error {
        ok,
        insufficientPrivileges,
        invalidUrlId
    }

    public static EnableUrlResponse success(Long urlId, boolean enable){
        return builder().error(Error.ok).id(urlId).enable(enable).build();
    }

    public static EnableUrlResponse failed(Error error){
        return builder().error(error).id(-1L).build();
    }


}
