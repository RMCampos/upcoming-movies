import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { SafeUrlPipe } from './safe-url.pipe';
import { environment } from '../environments/environment';

interface Movie {
  i18nTitle: string;
  originalTitle: string;
  cast: string[];
  directors: string[];
  summary: string;
  launchDate: string;
  youtubePreviewUrl: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [CommonModule, SafeUrlPipe],
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  movies: Movie[] = [];
  today = new Date();
  private apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<Movie[]>(`${this.apiUrl}/api/upcoming-movies`).subscribe(data => {
      this.movies = data;
    });
  }
}