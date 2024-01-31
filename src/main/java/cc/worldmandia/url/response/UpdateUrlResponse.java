package cc.worldmandia.url.response;


import cc.worldmandia.url.Url;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUrlResponse {

    private Error error;
    private Url updateUrl;

    public enum Error {
        ok,
        insufficientPrivileges,
        invalidUrlId,
        invalidTitleLength,
        invaliedDescriptionLength,
        invalidUrl
    }

    public static UpdateUrlResponse success(Url updateUrl){
        return builder().error(Error.ok).updateUrl(updateUrl).build();
    }

    public static UpdateUrlResponse failed(Error error){
        return builder().error(error).updateUrl(null).build();
    }
}
