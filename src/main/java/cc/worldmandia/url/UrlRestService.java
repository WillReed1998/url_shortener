package cc.worldmandia.url;

import cc.worldmandia.url.request.CreateUrlRequest;
import cc.worldmandia.url.request.EnableUrlRequest;
import cc.worldmandia.url.request.UpdateUrlRequest;
import cc.worldmandia.url.response.*;

import cc.worldmandia.user.User;
import cc.worldmandia.user.UserRepository;
import cc.worldmandia.user.UserServiceImpl;
import cc.worldmandia.util.UrlShortener.ShortUrlUtil;
import cc.worldmandia.util.UrlValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Tag(name = "Main methods")
@Service
@RequiredArgsConstructor
public class UrlRestService {

    private final UrlRepository urlRepository;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final ShortUrlUtil shortUrlUtil;

    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 255;

    public CreateUrlResponse create(String email, CreateUrlRequest request){
        Optional<CreateUrlResponse.Error> validationError = validateCreateFields(request);

        if (validationError.isPresent()){
            return CreateUrlResponse.failed(validationError.get());
        }

        User user = userService.findByEmail(email);

        if (!UrlValidator.validUrl(request.getFullUrl())){
            return  CreateUrlResponse.failed(CreateUrlResponse.Error.invalidUrl);
        }

        Url url = urlRepository.save(Url.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .fullUrl(request.getFullUrl())
                .shortUrl(shortUrlUtil.generateUniqueKey())
                .enabled(true)
                .build());

        return  CreateUrlResponse.success(url.getId());
    }

    public GetUserUrlsResponse getUserUrls(String email){
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()){
            List<Url> urls = urlRepository.getUserUrls(user.get().getId());
            return GetUserUrlsResponse.success(urls);
        }

        return GetUserUrlsResponse.failed();
    }

    public GetUserUrlResponse getUserUrl(String email, Long id){
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()){
            List<Url> urls = urlRepository.getUserUrls(user.get().getId());
            Url url = urls.stream().filter(url1 -> url1.getId().equals(id)).toList().get(0);
            return GetUserUrlResponse.success(url);
        }

        return GetUserUrlResponse.failed();
    }

    public EnableUrlResponse changeEnable(String email, EnableUrlRequest request){
        Optional<Url> urlOptional = urlRepository.findById(request.getId());

        if (urlOptional.isEmpty()) {
            return EnableUrlResponse.failed(EnableUrlResponse.Error.invalidUrlId);
        }

        Url url = urlOptional.get();
        boolean isNotUserUrl = isNotUserUrl(email, url);

        if (isNotUserUrl){
            return EnableUrlResponse.failed(EnableUrlResponse.Error.insufficientPrivileges);
        }

        url.setEnabled(request.isEnable());
        urlRepository.save(url);

        return EnableUrlResponse.success(url.getId(), url.isEnabled());
    }

    public UpdateUrlResponse update(String email, UpdateUrlRequest request){
        Optional<Url> optionalUrl = urlRepository.findById(request.getId());

        if (optionalUrl.isEmpty()){
            return UpdateUrlResponse.failed(UpdateUrlResponse.Error.invalidUrlId);
        }

        Url url = optionalUrl.get();
        boolean isNotUserUrl = isNotUserUrl(email, url);

        if (isNotUserUrl) {
            return UpdateUrlResponse.failed(UpdateUrlResponse.Error.insufficientPrivileges);
        }

        Optional<UpdateUrlResponse.Error> validationError = validateUpdateFields(request);

        if (validationError.isPresent()){
            return UpdateUrlResponse.failed(validationError.get());
        }

        if (!UrlValidator.validUrl(request.getFullUrl())){
            return  UpdateUrlResponse.failed(UpdateUrlResponse.Error.invalidUrl);
        }

        url.setFullUrl(request.getFullUrl());
        url.setShortUrl(shortUrlUtil.generateUniqueKey());
        url.setTitle(request.getTitle());
        url.setDescription(request.getDescription());

        urlRepository.save(url);

        return  UpdateUrlResponse.success(url);
    }

    public DeleteUrlResponse delete(String email, Long idUrl){
        Optional<Url> optionalUrl = urlRepository.findById(idUrl);

        if (optionalUrl.isEmpty()){
            return DeleteUrlResponse.failed(DeleteUrlResponse.Error.invalidUrlId);
        }

        Url url = optionalUrl.get();
        boolean isNotUserUrl = isNotUserUrl(email, url);

        if (isNotUserUrl){
            return DeleteUrlResponse.failed(DeleteUrlResponse.Error.insufficientPrivileges);
        }

        urlRepository.delete(url);

        return DeleteUrlResponse.success();
    }


    //CHECKING
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

    private boolean isNotUserUrl(String email, Url url){
        return !url.getUser().getEmail().equals(email);
    }

}