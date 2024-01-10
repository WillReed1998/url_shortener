package cc.worldmandia.url.response;

import cc.worldmandia.url.Url;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserUrlResponse {

    private Error error;
    private Url userUrl;

    public enum Error {
        ok,
        failed,
        invalidUrlId
    }

    public static GetUserUrlResponse success(Url userUrl){
        return builder().error(Error.ok).userUrl(userUrl).build();
    }

    public static GetUserUrlResponse failed(Error error){
        return builder().error(error).userUrl(null).build();
    }
}
