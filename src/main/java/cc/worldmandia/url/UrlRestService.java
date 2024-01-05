package cc.worldmandia.url;

import cc.worldmandia.url.request.CreateUrlRequest;
import cc.worldmandia.url.request.UpdateUrlRequest;
import cc.worldmandia.url.response.CreateUrlResponse;
import cc.worldmandia.url.response.DeleteUrlResponse;
import cc.worldmandia.url.response.GetUserUrlsResponse;
import cc.worldmandia.url.response.UpdateUrlResponse;

import cc.worldmandia.user.User;
import cc.worldmandia.user.UserRepository;
import cc.worldmandia.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UrlRestService {

    private final UrlRepository urlRepository;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 255;

    public CreateUrlResponse create(String email, CreateUrlRequest request){
        Optional<CreateUrlResponse.Error> validationError = validateCreateFields(request);

        if (validationError.isPresent()){
            return CreateUrlResponse.failed(validationError.get());
        }

        User user = userService.findByEmail(email);

        Url createUrl = urlRepository.save(Url.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .fullUrl(request.getFullUrl())
                //    .shortUrl()
                .build());

        return  CreateUrlResponse.success(createUrl.getId());
    }

    public GetUserUrlsResponse getUserUrls(String username){
        Optional<User> user = userRepository.findByEmail(username);
        List<Url> urls = urlRepository.getUserUrls(user.get().getId());

        return GetUserUrlsResponse.success(urls);
    }

    public UpdateUrlResponse update(String username, UpdateUrlRequest request){
        Optional<Url> optionalUrl = urlRepository.findById(request.getId());

        if (optionalUrl.isEmpty()){
            return UpdateUrlResponse.failed(UpdateUrlResponse.Error.invalidNodeId);
        }

        Url url = optionalUrl.get();
        boolean isNotUserUrl = isNotUserUrl(username, url);

        if (isNotUserUrl) {
            return UpdateUrlResponse.failed(UpdateUrlResponse.Error.insufficientPrivileges);
        }

        Optional<UpdateUrlResponse.Error> validationError = validateUpdateFields(request);

        if (validationError.isPresent()){
            return UpdateUrlResponse.failed(validationError.get());
        }

        url.setFullUrl(request.getFullUrl());
        //url.setShortUrl();
        url.setTitle(request.getTitle());
        url.setDescription(request.getDescription());

        urlRepository.save(url);

        return  UpdateUrlResponse.success(url);
    }

    public DeleteUrlResponse delete(String username, Long idUrl){
        Optional<Url> optionalUrl = urlRepository.findById(idUrl);

        if (optionalUrl.isEmpty()){
            return DeleteUrlResponse.failed(DeleteUrlResponse.Error.invalidUrlId);
        }

        Url url = optionalUrl.get();
        boolean isNotUserUrl = isNotUserUrl(username, url);

        if (isNotUserUrl){
            return DeleteUrlResponse.failed(DeleteUrlResponse.Error.insufficientPrivileges);
        }

        urlRepository.delete(url);

        return DeleteUrlResponse.success();
    }

    private Optional<CreateUrlResponse.Error> validateCreateFields(CreateUrlRequest request){
        if (Objects.isNull(request.getTitle()) || request.getTitle().length() > MAX_TITLE_LENGTH) {
            return Optional.of(CreateUrlResponse.Error.invalidTitle);
        }

        if (Objects.isNull(request.getDescription()) || request.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            return Optional.of(CreateUrlResponse.Error.invalidDescription);
        }

        return Optional.empty();
    }

    private Optional<UpdateUrlResponse.Error> validateUpdateFields(UpdateUrlRequest request){
        if (Objects.nonNull(request.getTitle()) && request.getTitle().length() > MAX_TITLE_LENGTH) {
            return Optional.of(UpdateUrlResponse.Error.invalidTitleLength);
        }

        if (Objects.nonNull(request.getDescription()) && request.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            return Optional.of(UpdateUrlResponse.Error.invaliedDescriptionLength);
        }

        return Optional.empty();
    }

    private boolean isNotUserUrl(String username, Url url){
        return !url.getUser().getEmail().equals(username);
    }

}