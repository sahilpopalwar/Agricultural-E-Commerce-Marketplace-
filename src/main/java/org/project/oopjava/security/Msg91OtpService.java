package org.project.oopjava.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class Msg91OtpService {
    private final RestClient restClient;
    private final String authKey;
    private final String templateId;
    private final String countryCode;

    public Msg91OtpService(
            RestClient.Builder restClientBuilder,
            @Value("${app.otp.msg91.endpoint}") String endpoint,
            @Value("${app.otp.msg91.auth-key}") String authKey,
            @Value("${app.otp.msg91.template-id}") String templateId,
            @Value("${app.otp.msg91.country-code:91}") String countryCode) {
        this.restClient = restClientBuilder.baseUrl(endpoint).build();
        this.authKey = authKey;
        this.templateId = templateId;
        this.countryCode = countryCode;
    }

    public void send(String phone, String otp) {
        if (authKey.isBlank() || templateId.isBlank()) {
            throw new OtpDeliveryException(
                "MSG91 OTP is not configured. Set MSG91_AUTH_KEY and MSG91_TEMPLATE_ID.");
        }
        try {
            Map<?, ?> response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("template_id", templateId)
                    .queryParam("mobile", toMsg91Mobile(phone))
                    .queryParam("otp", otp)
                    .build())
                .header("authkey", authKey)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class);

            if (response == null || !"success".equalsIgnoreCase(String.valueOf(response.get("type")))) {
                throw new OtpDeliveryException("MSG91 rejected the OTP request.");
            }
        } catch (OtpDeliveryException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new OtpDeliveryException("Unable to send OTP through MSG91.", ex);
        }
    }

    private String toMsg91Mobile(String phone) {
        return phone.startsWith(countryCode) ? phone : countryCode + phone;
    }
}
