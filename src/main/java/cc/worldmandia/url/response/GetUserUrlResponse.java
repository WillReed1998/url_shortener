package cc.worldmandia.url.response;

import cc.worldmandia.url.Url;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserUrlResponse {

    private GetUserUrlsResponse.Error error;
    private Url userUrl;

    public enum Error {
        ok,
        failed
    }

    public static GetUserUrlResponse success(Url userUrl){
        return builder().error(GetUserUrlsResponse.Error.ok).userUrl(userUrl).build();
    }

    public static GetUserUrlResponse failed(){
        return builder().error(GetUserUrlsResponse.Error.failed).userUrl(null).build();
    }
}
