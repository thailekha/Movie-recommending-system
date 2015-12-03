package models;

import com.google.common.base.Objects;

import utils.ToJsonString;
import utils.Valid;

public class Movie {
	private static long counter = 1;
	private long movieId;
	private String title, releaseDate, url, genreCode;

	public Movie(String title, String releaseDate, String url, String genreCode) {
		movieId = counter++;
		this.title = Valid.str(title, 200, "default title");
		this.releaseDate = Valid.str(releaseDate, 200, "default date");
		this.url = Valid.str(url, 200, "default url");
		this.genreCode = Valid.str(genreCode, 19, "1" + Valid.autoStr('0', 18));
	}

	public long getMovieId() {
		return movieId;
	}

	public String getTitle() {
		return title;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getUrl() {
		return url;
	}

	public String getGenreCode() {
		return genreCode;
	}

	public String toString() {
		return new ToJsonString(getClass(), this).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.title, this.releaseDate, this.url, this.genreCode);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Movie) {
			final Movie other = (Movie) obj;
			return Objects.equal(title, other.getTitle()) && Objects.equal(releaseDate, other.getReleaseDate())
					&& Objects.equal(url, other.getUrl()) && Objects.equal(genreCode, other.getGenreCode());
		} else {
			return false;
		}
	}

	public static void resetCounter() {
		counter = 1;
	}
}
