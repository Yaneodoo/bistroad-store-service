package kr.bistroad.storeservice.global.config.mongo

import kr.bistroad.storeservice.global.domain.Coordinate
import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter

interface CoordinateConverter {
    @WritingConverter
    class CoordinateWriter : Converter<Coordinate, Document> {
        override fun convert(source: Coordinate): Document = Document().apply {
            put("type", "Point")
            put("coordinates", listOf(source.lng, source.lat))
        }
    }

    @ReadingConverter
    class CoordinateReader : Converter<Document, Coordinate> {
        override fun convert(source: Document) = with(source["coordinates"] as List<*>) {
            Coordinate(
                lat = this[1] as Double,
                lng = this[0] as Double
            )
        }
    }
}