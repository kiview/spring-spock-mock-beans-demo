/*
 * Spring Spock Mock Demo Application
 *  Copyright (C)2016 Kevin Wittek
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.groovycoder

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@SpringBootTest
class BookServiceTests extends Specification {

    @Autowired
    BookService bookService

    @Autowired
    BookRepository stubBookRepository

    def "returns no books if no books available"() {
        given: "no books"
        stubBookRepository.findAll() >> []

        when: "retrieving available books"
        def availableBooks = bookService.listAvailableBooks()

        then: "there a no books available"
        availableBooks.isEmpty()
    }

    def "returns available books"() {
        given: "some books"
        stubBookRepository.findAll() >> [
                new Book("The Color of Magic", "T. P.", true),
                new Book("There and Back Again", "B. B.", true),
                new Book("Do Androids Dream of Electric Sheep?", "P. K. D.", false),
        ]

        when: "retrieving available books"
        def availableBooks = bookService.listAvailableBooks()

        then: "there a some books available"
        availableBooks.size() == 2
    }

    @TestConfiguration
    static class Config {
        private DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        BookRepository bookRepository() {
            factory.Stub(BookRepository, name: "bookRepository")
        }
    }

}
