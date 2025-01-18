package ru.yandex.practicum.client.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.client.model.EndpointHit;
import ru.yandex.practicum.client.model.ViewStats;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-13T17:34:54.685156800+03:00[Europe/Moscow]", comments = "Generator version: 7.8.0")
@Validated
@Tag(name = "stats-service", description = "API для работы со статистикой посещений")
public interface StatsServiceApi {

    /**
     * GET /stats : Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать (например используя java.net.URLEncoder.encode) 
     *
     * @param start Дата и время начала диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;) (required)
     * @param end Дата и время конца диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;) (required)
     * @param uris Список uri для которых нужно выгрузить статистику (optional)
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip) (optional, default to false)
     * @return Статистика собрана (status code 200)
     */
    @Operation(
            operationId = "getStats",
            summary = "Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать (например используя java.net.URLEncoder.encode) ",
            tags = {"stats-service"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статистика собрана", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ViewStats.class)))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/stats",
            produces = {"application/json"}
    )
    ResponseEntity<List<ViewStats>> getStats(
            @NotNull @Parameter(name = "start", description = "Дата и время начала диапазона за который нужно выгрузить статистику (в формате \"yyyy-MM-dd HH:mm:ss\")", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "start", required = true) String start,
            @NotNull @Parameter(name = "end", description = "Дата и время конца диапазона за который нужно выгрузить статистику (в формате \"yyyy-MM-dd HH:mm:ss\")", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "end", required = true) String end,
            @Parameter(name = "uris", description = "Список uri для которых нужно выгрузить статистику", in = ParameterIn.QUERY) @Valid @RequestParam(value = "uris", required = false) List<String> uris,
            @Parameter(name = "unique", description = "Нужно ли учитывать только уникальные посещения (только с уникальным ip)", in = ParameterIn.QUERY) @Valid @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique
    );


    /**
     * POST /hit : Сохранение информации о том, что к эндпоинту был запрос
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем. Название сервиса, uri и ip пользователя указаны в теле запроса.
     *
     * @param endpointHit данные запроса (required)
     * @return Информация сохранена (status code 201)
     */
    @Operation(
            operationId = "hit",
            summary = "Сохранение информации о том, что к эндпоинту был запрос",
            description = "Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем. Название сервиса, uri и ip пользователя указаны в теле запроса.",
            tags = {"stats-service"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Информация сохранена")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/hit",
            consumes = "application/json"
    )
    ResponseEntity<Void> hit(
            @Parameter(name = "EndpointHit", description = "данные запроса", required = true) @Valid @RequestBody EndpointHit endpointHit
    );
}
