package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * ViewStats
 */

public class ViewStats {

    private String app;

    private String uri;

    private Long hits;

    public ViewStats app(String app) {
        this.app = app;
        return this;
    }

    /**
     * Название сервиса
     *
     * @return app
     */

    @Schema(name = "app", example = "ewm-main-service", description = "Название сервиса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("app")
    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public ViewStats uri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * URI сервиса
     *
     * @return uri
     */

    @Schema(name = "uri", example = "/events/1", description = "URI сервиса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ViewStats hits(Long hits) {
        this.hits = hits;
        return this;
    }

    /**
     * Количество просмотров
     *
     * @return hits
     */

    @Schema(name = "hits", example = "6", description = "Количество просмотров", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("hits")
    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ViewStats viewStats = (ViewStats) o;
        return Objects.equals(this.app, viewStats.app) &&
               Objects.equals(this.uri, viewStats.uri) &&
               Objects.equals(this.hits, viewStats.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ViewStats {\n");
        sb.append("    app: ").append(toIndentedString(app)).append("\n");
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
        sb.append("    hits: ").append(toIndentedString(hits)).append("\n");
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

