package org.petmarket.fake;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.petmarket.advertisements.advertisement.dto.AdvertisementDetailsResponseDto;
import org.petmarket.advertisements.advertisement.dto.AdvertisementRequestDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.entity.AdvertisementType;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.advertisements.category.entity.AdvertisementCategory;
import org.petmarket.advertisements.category.entity.AdvertisementCategoryTranslate;
import org.petmarket.advertisements.category.repository.AdvertisementCategoryRepository;
import org.petmarket.advertisements.images.service.AdvertisementImageService;
import org.petmarket.breeds.entity.Breed;
import org.petmarket.breeds.entity.BreedTranslation;
import org.petmarket.breeds.repository.BreedRepository;
import org.petmarket.delivery.repository.DeliveryRepository;
import org.petmarket.location.entity.City;
import org.petmarket.location.repository.CityRepository;
import org.petmarket.options.service.OptionsService;
import org.petmarket.payment.repository.PaymentRepository;
import org.petmarket.users.dto.UserRequestDto;
import org.petmarket.users.dto.UserResponseDto;
import org.petmarket.users.dto.UserUpdateRequestDto;
import org.petmarket.users.entity.User;
import org.petmarket.users.repository.UserRepository;
import org.petmarket.users.service.UserAuthService;
import org.petmarket.users.service.UserService;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RequiredArgsConstructor
@Service
public class FakeDataService {

    private final UserAuthService userAuthService;
    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final AdvertisementImageService advertisementImageService;
    private final OptionsService optionsService;

    private final BreedRepository breedRepository;
    private final AdvertisementCategoryRepository categoryRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void createUsers(int count) {

        Faker fakerEn = new Faker(new Locale("en"));
        Faker faker = new Faker(new Locale("uk"));

        for (int i = 0; i < count; i++) {
            User user;
            String email = fakerEn.internet().emailAddress();
            try {
                user = userService.findByUsername(email);
                System.out.println("email " + email + " found");
            } catch (Exception e) {
                UserRequestDto userDto = UserRequestDto.builder()
                        .email(email)
                        .password(fakerEn.internet().password(8, 10, true, true))
                        .rememberMe(false).build();
                UserResponseDto register = userAuthService.register(userDto, new SimpleBindingResult());

                user = userService.findById(register.getId());
                UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
                userUpdateRequestDto.setEmail(user.getEmail());
                userUpdateRequestDto.setFirstName(faker.name().firstName());
                userUpdateRequestDto.setLastName(faker.name().lastName());

                Date birthday = faker.date().birthday(15, 105);
                Instant instant = birthday.toInstant();
                LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                userUpdateRequestDto.setBirthday(localDate);
                userUpdateRequestDto.setMainPhone(faker.phoneNumber().phoneNumber());
                userUpdateRequestDto.setPhones(new HashSet<>());

                userService.updateUser(user, userUpdateRequestDto);
            }
        }
    }

    public void createAdvertisements(int count) {
        for (int i = 0; i < count; i++) {
            AdvertisementRequestDto requestDto = AdvertisementRequestDto.builder()
                    .quantity(1)
                    .cityId(getRandomCityId())
                    .type(AdvertisementType.SIMPLE)
                    .deliveriesIds(getRandomDeliveriesIds())
                    .paymentsIds(getRandomPaymentsIds())
                    .build();

            Document document = fillDtoFromOlx(requestDto);
            if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
                continue;
            }
            AdvertisementDetailsResponseDto responseDto = advertisementService.addAdvertisement(
                    requestDto, new SimpleBindingResult(), getRandomEmail()
            );
            Advertisement advertisement = advertisementService.getAdvertisement(responseDto.getId());
            advertisement.setImages(new HashSet<>());
            uploadImages(advertisement, document);
        }
    }

    private List<Long> getRandomPaymentsIds() {
        return paymentRepository.findRandomEntities(1).stream().map(e -> e.getId()).toList();
    }

    private List<Long> getRandomDeliveriesIds() {
        return deliveryRepository.findRandomEntities(2).stream().map(e -> e.getId()).toList();
    }

    private void uploadImages(Advertisement advertisement, Document document) {
        try {
            Elements imagesElements = document.select("img.css-1bmvjcs");
            List<MultipartFile> files = new ArrayList<>();
            for (Element element : imagesElements) {
                if (files.size() == 5) {
                    break;
                }
                String src = element.attr("src");
                files.add(downloadImageAsMultipartFile(src));
            }
            advertisementImageService.uploadImages(advertisement, files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal getRandomPrice() {
        double randomValue = Math.random() * (5000 - 500) + 500;
        BigDecimal result = BigDecimal.valueOf(randomValue).setScale(0, RoundingMode.HALF_UP);
        return result;
    }

    private Document fillDtoFromOlx(AdvertisementRequestDto requestDto) {
        AdvertisementCategory category = categoryRepository.findRandomEntity();
        Breed breed = breedRepository.findRandomEntityByCategoryId(category.getId());

        if (breed != null) {
            requestDto.setBreedId(breed.getId());
        }

        if (category != null) {
            requestDto.setCategoryId(category.getId());
        }

        String hrefLink = "";
        String pageQuery = "";
        Random random = new Random();
        int page = random.nextInt(20);
        if (page > 1) {
            pageQuery = "?page=" + page;
        }
        if (requestDto.getCategoryId() == 4) {
            hrefLink = getRandomOlxLinkOnPage(
                    getDocumentFromLink("https://www.olx.ua/uk/zhivotnye/akvariumnye-rybki/" + pageQuery)
            );
        } else if (requestDto.getCategoryId() == 5) {
            hrefLink = getRandomOlxLinkOnPage(
                    getDocumentFromLink("https://www.olx.ua/uk/zhivotnye/selskohozyaystvennye-zhivotnye/" +
                            pageQuery)
            );
        } else if (requestDto.getCategoryId() == 3) {
            hrefLink = getRandomOlxLinkOnPage(
                    getDocumentFromLink("https://www.olx.ua/uk/zhivotnye/ptitsy/" + pageQuery)
            );
        } else if (requestDto.getCategoryId() == 1) {
            hrefLink = getRandomOlxLinkOnPage(
                    getDocumentFromLink("https://www.olx.ua/uk/zhivotnye/sobaki/" + pageQuery)
            );
        } else if (requestDto.getCategoryId() == 2) {
            hrefLink = getRandomOlxLinkOnPage(
                    getDocumentFromLink("https://www.olx.ua/uk/zhivotnye/koshki/" + pageQuery)
            );
        } else if (requestDto.getCategoryId() == 8) {
            hrefLink = getRandomOlxLinkOnPage(
                    getDocumentFromLink("https://www.olx.ua/uk/zhivotnye/gryzuny/" + pageQuery)
            );
        } else {
            String queryString;
            if (breed == null) {
                AdvertisementCategoryTranslate categoryTranslate = category.getTranslations().stream()
                        .filter(t -> optionsService.getDefaultSiteLanguage().equals(t.getLanguage()))
                        .findFirst().get();
                queryString = categoryTranslate.getTitle();
            } else {
                BreedTranslation breedTranslation = breed.getTranslations().stream()
                        .filter(t -> optionsService.getDefaultSiteLanguage().equals(t.getLanguage()))
                        .findFirst().get();
                queryString = breedTranslation.getTitle();
            }
            hrefLink = getRandomOlxLink(queryString);
        }

        return processOlxLink(hrefLink, requestDto);
    }

    private String getRandomOlxLinkOnPage (Document document) {
        Random random = new Random();
        Elements links = document.select("a.css-rc5s2u");
        List<String> hrefList = new ArrayList<>();
        for (Element link : links) {
            String href = link.attr("href");
            hrefList.add(href);
        }
        int randomIndex = random.nextInt(hrefList.size());
        return "https://www.olx.ua" + hrefList.get(randomIndex);
    }

    private Document getDocumentFromLink(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRandomOlxLink(String queryString) {
        Random random = new Random();
        Document document = getDocumentFromLink("https://www.olx.ua/uk/list/q-" +
                queryString + "/?page=" + random.nextInt(10)
        );
        return getRandomOlxLinkOnPage(document);
    }

    private Document processOlxLink(String hrefLink, AdvertisementRequestDto requestDto) {
        try {
            Document document = Jsoup.connect(hrefLink).get();
            addPrice(requestDto, document);
            addTitle(requestDto, document);
            addDescription(requestDto, document);
            return document;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addDescription(AdvertisementRequestDto requestDto, Document document) {
        Elements descriptionElements = document.select("div.css-1t507yq.er34gjf0");
        for (Element element : descriptionElements) {
            requestDto.setDescription(element.text());
        }
    }

    private void addTitle(AdvertisementRequestDto requestDto, Document document) {
        Elements titleElements = document.select("h4.css-1juynto");
        for (Element element : titleElements) {
            requestDto.setTitle(element.text());
        }
    }

    private void addPrice(AdvertisementRequestDto requestDto, Document document) {
        Elements priceElements = document.select("h3.css-12vqlj3");
        for (Element element : priceElements) {
            String priceText = element.text();
            if ("Безкоштовно".equals(priceText)) {
                requestDto.setPrice(BigDecimal.ZERO);
            } else {
                int course = 1;
                if (priceText.contains(" $")) {
                    priceText = priceText.replaceAll(" $", "");
                    course = 38;
                }
                if (priceText.contains(" €")) {
                    priceText = priceText.replaceAll(" €", "");
                    course = 42;
                }
                priceText = priceText.replaceAll(" грн.", "");
                priceText = priceText.replaceAll(" ", "");
                try {
                    double price = Double.valueOf(priceText) * course;
                    BigDecimal priceBigDecimal = BigDecimal.valueOf(price).setScale(0, RoundingMode.HALF_UP);
                    requestDto.setPrice(priceBigDecimal);
                } catch (Exception e) {
                    requestDto.setPrice(getRandomPrice());
                }

            }
        }
    }

    public MultipartFile downloadImageAsMultipartFile(String imageUrl) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl, byte[].class);
        byte[] imageBytes = response.getBody();
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        MultipartFile multipartFile = new MockMultipartFile("file", "filename.webp", "image/webp", inputStream);

        return multipartFile;
    }

    private long getRandomCityId() {
        City city = cityRepository.findRandomEntity();
        return city.getId();
    }

    private String getRandomEmail() {
        User user = userRepository.findRandomEntity();
        return user.getEmail();
    }

    private class SimpleBindingResult implements BindingResult {

        @Override
        public Object getTarget() {
            return null;
        }

        @Override
        public Map<String, Object> getModel() {
            return null;
        }

        @Override
        public Object getRawFieldValue(String field) {
            return null;
        }

        @Override
        public PropertyEditor findEditor(String field, Class<?> valueType) {
            return null;
        }

        @Override
        public PropertyEditorRegistry getPropertyEditorRegistry() {
            return null;
        }

        @Override
        public String[] resolveMessageCodes(String errorCode) {
            return new String[0];
        }

        @Override
        public String[] resolveMessageCodes(String errorCode, String field) {
            return new String[0];
        }

        @Override
        public void addError(ObjectError error) {

        }

        @Override
        public String getObjectName() {
            return null;
        }

        @Override
        public String getNestedPath() {
            return null;
        }

        @Override
        public void setNestedPath(String nestedPath) {

        }

        @Override
        public void pushNestedPath(String subPath) {

        }

        @Override
        public void popNestedPath() throws IllegalStateException {

        }

        @Override
        public void reject(String errorCode) {

        }

        @Override
        public void reject(String errorCode, String defaultMessage) {

        }

        @Override
        public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {

        }

        @Override
        public void rejectValue(String field, String errorCode) {

        }

        @Override
        public void rejectValue(String field, String errorCode, String defaultMessage) {

        }

        @Override
        public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {

        }

        @Override
        public void addAllErrors(Errors errors) {

        }

        @Override
        public boolean hasErrors() {
            return false;
        }

        @Override
        public int getErrorCount() {
            return 0;
        }

        @Override
        public List<ObjectError> getAllErrors() {
            return null;
        }

        @Override
        public boolean hasGlobalErrors() {
            return false;
        }

        @Override
        public int getGlobalErrorCount() {
            return 0;
        }

        @Override
        public List<ObjectError> getGlobalErrors() {
            return null;
        }

        @Override
        public ObjectError getGlobalError() {
            return null;
        }

        @Override
        public boolean hasFieldErrors() {
            return false;
        }

        @Override
        public int getFieldErrorCount() {
            return 0;
        }

        @Override
        public List<FieldError> getFieldErrors() {
            return null;
        }

        @Override
        public FieldError getFieldError() {
            return null;
        }

        @Override
        public boolean hasFieldErrors(String field) {
            return false;
        }

        @Override
        public int getFieldErrorCount(String field) {
            return 0;
        }

        @Override
        public List<FieldError> getFieldErrors(String field) {
            return null;
        }

        @Override
        public FieldError getFieldError(String field) {
            return null;
        }

        @Override
        public Object getFieldValue(String field) {
            return null;
        }

        @Override
        public Class<?> getFieldType(String field) {
            return null;
        }
    }
}
