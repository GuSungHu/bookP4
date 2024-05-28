import React,{useState} from "react";
import axios from 'axios';

const BookSearch = () => {
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [books, setBooks] = useState([]);

    const handleSearch = () => {
        const params = {};
        if (title) params.title = title;
        if (author) params.author = author;

        axios.get('/api/books/search',{params})
        .then(response => {
        setBooks(response.data);
    })
    .catch(error => {
    console.error("There was an error fetching the books!" , error);
    });
};

return(
    <div>
        <h1>Search Books</h1>
        <div>
    <input
    type="text"
    placeholder="Title"
    value={title}
    onChange={(e) => setTitle(e.target.value)}/>

    <input
    type="text"
    placeholder="Author"
    value={author}
    onChange={(e) => setAuthor(e.target.value)}/>

<button onClick={handleSearch}>Search</button>
    </div>
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

export default BookSearch;


