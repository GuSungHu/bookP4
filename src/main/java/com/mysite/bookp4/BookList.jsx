import React, {useEffect, useState} from "react";
import axios from 'axios';
import {Link} from 'react-router-dom';


const BookList = () => {
    const [books, setBooks] = useState([]);

    useEffect(() => {
            axios.get('/api/books')
                .then(response => {
                    setBooks(response.data);
                })
                .catch(error => {
                    console.error("There was an error fetching the books!" , error);
                });
    }, []);

    return(
        <div>
            <h1> Book List </h1>
            <Link to="/create">Add New Book</Link>
            <ul>
                {books.map(book => (
                <li key={book.id}>
                    {book.title} by {book.author}
                </li>
                ))}
            </ul>
        </div>
    );
}

export default BookList;