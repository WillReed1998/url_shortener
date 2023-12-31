package cc.worldmandia.restApi.response;

import cc.worldmandia.url.Url;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUserUrlsResponse {

    private Error error;
    private List<Url> userUrls;

    public enum Error {
        ok,
        failed
    }

    public static GetUserUrlsResponse success(List<Url> userUrls){
        return builder().error(Error.ok).userUrls(userUrls).build();
    }

    public static GetUserUrlsResponse failed(){
        return builder().error(Error.failed).userUrls(null).build();
    }
}