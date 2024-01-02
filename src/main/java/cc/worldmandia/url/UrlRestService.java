package cc.worldmandia.url;

import cc.worldmandia.url.request.CreateUrlRequest;
import cc.worldmandia.url.request.UpdateUrlRequest;
import cc.worldmandia.url.response.CreateUrlResponse;
import cc.worldmandia.url.response.DeleteUrlResponse;
import cc.worldmandia.url.response.GetUserUrlsResponse;
import cc.worldmandia.url.response.UpdateUrlResponse;
import cc.worldmandia.url.Url;
import cc.worldmandia.url.UrlRepository;
import cc.worldmandia.user.User;
import cc.worldmandia.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
public class UrlRestService {

    private final UrlRepository urlRepository;
    private final UserService userService;

    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 255;

    public CreateUrlResponse create(Long id, CreateUrlRequest request){
        Optional<CreateUrlResponse.Error> validationError = validateCreateFields(request);

        if (validationError.isPresent()){
            return CreateUrlResponse.failed(validationError.get());
        }

        User user = userService.findById(id);

        Set<User> users = new HashSet<>();
        users.add(user);

        Url createUrl = urlRepository.save(Url.builder()
                .users(users)
                .title(request.getTitle())
                .description(request.getDescription())
                .fullUrl(request.getFullUrl())
             //   .shortUrl()
                .build());

        return  CreateUrlResponse.success(createUrl.getId());
    }

    public GetUserUrlsResponse getUserUrls(Long id){
        List<Url> userUrls = urlRepository.getUserUrls(id);
        return GetUserUrlsResponse.success(userUrls);
    }

    public UpdateUrlResponse update(Long id, UpdateUrlRequest request){
        Optional<Url> optionalUrl = urlRepository.findById(request.getId());

        if (optionalUrl.isEmpty()){
            return UpdateUrlResponse.failed(UpdateUrlResponse.Error.invalidNodeId);
        }

        Url url = optionalUrl.get();
        boolean isNotUserUrl = isNotUserUrl(id, url);

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

    public DeleteUrlResponse delete(Long idUser, Long idUrl){
        Optional<Url> optionalUrl = urlRepository.findById(idUrl);

        if (optionalUrl.isEmpty()){
            return DeleteUrlResponse.failed(DeleteUrlResponse.Error.invalidUrlId);
        }

        Url url = optionalUrl.get();
        boolean isNotUserUrl = isNotUserUrl(idUser, url);

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

    //validateUpdateFields
    private Optional<UpdateUrlResponse.Error> validateUpdateFields(UpdateUrlRequest request){
        if (Objects.nonNull(request.getTitle()) && request.getTitle().length() > MAX_TITLE_LENGTH) {
            return Optional.of(UpdateUrlResponse.Error.invalidTitleLength);
        }

        if (Objects.nonNull(request.getDescription()) && request.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            return Optional.of(UpdateUrlResponse.Error.invaliedDescriptionLength);
        }

        return Optional.empty();
    }


    private boolean isNotUserUrl(Long userId, Url url){
        return !url.getUsers().stream().findAny().get().getId().equals(userId);
    }

}