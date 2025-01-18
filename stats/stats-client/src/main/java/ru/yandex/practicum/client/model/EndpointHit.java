package ru.yandex.practicum.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

public class EndpointHit {

    private Long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;

    public EndpointHit id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Идентификатор записи
     *
     * @return id
     */

    @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "Идентификатор записи", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EndpointHit app(String app) {
        this.app = app;
        return this;
    }

    /**
     * Идентификатор сервиса для которого записывается информация
     *
     * @return app
     */

    @Schema(name = "app", example = "ewm-main-service", description = "Идентификатор сервиса для которого записывается информация", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("app")
    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public EndpointHit uri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * URI для которого был осуществлен запрос
     *
     * @return uri
     */

    @Schema(name = "uri", example = "/events/1", description = "URI для которого был осуществлен запрос", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public EndpointHit ip(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * IP-адрес пользователя, осуществившего запрос
     *
     * @return ip
     */

    @Schema(name = "ip", example = "192.163.0.1", description = "IP-адрес пользователя, осуществившего запрос", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public EndpointHit timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате \"yyyy-MM-dd HH:mm:ss\")
     *
     * @return timestamp
     */

    @Schema(name = "timestamp", example = "2022-09-06 11:00:23", description = "Дата и время, когда был совершен запрос к эндпоинту (в формате \"yyyy-MM-dd HH:mm:ss\")", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EndpointHit endpointHit = (EndpointHit) o;
        return Objects.equals(this.id, endpointHit.id) &&
               Objects.equals(this.app, endpointHit.app) &&
               Objects.equals(this.uri, endpointHit.uri) &&
               Objects.equals(this.ip, endpointHit.ip) &&
               Objects.equals(this.timestamp, endpointHit.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, uri, ip, timestamp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EndpointHit {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    app: ").append(toIndentedString(app)).append("\n");
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
        sb.append("    ip: ").append(toIndentedString(ip)).append("\n");
        sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

