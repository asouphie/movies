package com.example.asouphie.mini_projet;

import java.util.Map;

/**
 * Created by Asouphie on 31/05/2017.
 */

public class Search {
    private Long page;
    private Movie[] results;
    private Long total_results;
    private Long total_pages;


    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Movie[] getResults() {
        return results;
    }

    public void setResults(Movie[] results) {
        this.results = results;
    }

    public Long getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Long total_results) {
        this.total_results = total_results;
    }

    public Long getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Long total_pages) {
        this.total_pages = total_pages;
    }
}
