package cc.worldmandia.url.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteUrlResponse {

    private Error error;

    public enum Error{
        ok,
        insufficientPrivileges,
        invalidUrlId
    }

    public static DeleteUrlResponse success(){
        return builder().error(Error.ok).build();
    }

    public static DeleteUrlResponse failed(Error error){
        return builder().error(error).build();
    }
}
