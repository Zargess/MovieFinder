package moviefinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;


/**
 * Created by Helene on 21-02-14.
 */
public class MovieManager {

    private String movieFinderSavedMovies = "Settings\\MovieFinderSavedMovies.txt";
    private String movieFinderSavedCategories = "Settings\\MovieFinderSavedCategories.txt";

    private File[] mainDirs;
    private File extensionFile;
    private HashSet<Movie> movies = new HashSet();
    private HashSet<String> categories = new HashSet();
    private ArrayList<String> extensions = new ArrayList<>();

    public MovieManager(File[] mainDirs, File extensionFile){
        this.mainDirs = mainDirs;
        this.extensionFile = extensionFile;
        initialize();
    }

    /**
     * Initialize the MovieManager by loading the
     * movies and categories
     */
    public void initialize(){
        loadMoviesFromFile();
        loadCategoriesFromFile();
    }

    /*
     * Returns the list of categories
     */
    public ArrayList<String> getCategories(){
        return new ArrayList<>(categories);
    }

    /*
     * Updates the movie and category list
     */
    public void update(){
        System.out.println("Start updating");
        movies = new HashSet<>();
        categories = new HashSet<>();
        extensions = loadFile(extensionFile);
        updateMovieList();
        saveMovies();
        saveCategories();
        System.out.println("End updating");
    }

    /*
     * Search method
     * param: Gets a search string and a category
     * returns: an ArrayList of Movies fulfilling the search criteria.
     */
    public Set<Movie> search(String searchString, String category){
        Set<Movie> categoryList = searchCategory(category, movies);
        return searchString(searchString, categoryList);
    }




    // ////////////////////////////////////
    // Private initialize methods
    // ////////////////////////////////////

    private void loadMoviesFromFile(){
        try{
            File movieFile = new File(movieFinderSavedMovies);
            if(!movieFile.exists()){
                update();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(movieFile));
            String line = br.readLine();

            if(line == null || line.equals("")){
                update();
                return;
            }

            while(line != null){
                String mainDir = line;
                String categoryString = br.readLine();
                String title = br.readLine();

                if(categoryString != null && title != null){
                    String[] categories = categoryString.split(",");
                    Movie movie = new Movie(mainDir,categories,title);
                    movies.add(movie);
                }
                line = br.readLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadCategoriesFromFile(){
        try{
            File catFile = new File(movieFinderSavedCategories);
            if(!catFile.exists()){
                update();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(catFile));
            String line = br.readLine();

            if(line == null || line.equals("")){
                update();
                return;
            }

            while(line != null){
                String category = line;
                categories.add(category);
                line = br.readLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }




    // /////////////////////////////////////
    // Private Methods used to update the program
    // /////////////////////////////////////

    /**
     * Get all files in the directory, make them to movie object
     * and save them in the movie list
     */
    private void updateMovieList(){
        for(File dir : mainDirs){
            ArrayList<String> fileNames = getFiles(dir);

            for(String name : fileNames){
                Movie m = createMovieFromFile(name);
                movies.add(m);
            }
        }
    }

    /**
     *  Create a Movie triple from a file by looking at the files absolute name/path
     */
    private Movie createMovieFromFile(String absoluteName){
        int startIndex = absoluteName.indexOf("Film");
        int endIndex = absoluteName.lastIndexOf("\\");
        String substring = absoluteName.substring(startIndex,endIndex);
        String[] folders = substring.split("\\\\");

        String movieDir = folders[0];
        //movieDir = movieDir.replaceAll("Data","Film");
        String title = folders[folders.length-1];
        String[] movieCategories = new String[folders.length-2];
        for(int i = 1; i < folders.length-1; i++){
            String cat = folders[i];
            movieCategories[i-1] = cat;
            if(isCategory(cat)) categories.add(cat);
        }

        Movie movie = new Movie(movieDir, movieCategories, title);

        return movie;
    }

    private Boolean isCategory(String s){
        return s.equals(s.toUpperCase());
    }

    /**
     *  Depth first search to get all movie files.
     *
     *  If we get to a folder containing af non-directory file we only add this file
     *  to the list of all files and forget everything else in this folder
     */
    private ArrayList<String> getFiles(File dir){
        Stack<File> stack = new Stack<>();
        HashSet<String> fileNames = new HashSet<>();

        if(!dir.isDirectory()){
            System.out.println("Not directory");
            return new ArrayList<String>();
        }

        // Take the folders which contains "Film" in their name
        for(File f : dir.listFiles()){
            stack.add(f);
        }
        // Depth first search
        while (!stack.isEmpty()){
            File file = stack.pop();
            if(file.isDirectory()){
                // This file is a directory add all subfiles to the stack
                stack.addAll(Arrays.asList(file.listFiles()));
            }
            else if(correctExtension(file)){
                // This is a file (not dir) with the correct file extension
                fileNames.add(file.getAbsolutePath());
            }
        }

        return new ArrayList<>(fileNames);
    }

    /*
     * Check if the file has the correct file extension
     */
    private boolean correctExtension(File file){
        Boolean res = false;
        for(String ex : extensions){
            res = res || (file.getName().endsWith(ex));
        }
        return res;
    }

    /*
     * Save the list of movies to a file
     */
    private void saveMovies(){
        try{
            PrintWriter writer = new PrintWriter(movieFinderSavedMovies,"UTF-8");
            for(Movie m : movies){
                writer.println(m.mainDir);
                writer.println(m.categoriesToString());
                writer.println(m.title);
            }
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * Save the list of categories to a file
     */
    private void saveCategories(){
        try{
            PrintWriter writer = new PrintWriter(movieFinderSavedCategories,"UTF-8");
            ArrayList<String> cats = new ArrayList<>(categories);
            for(String s : cats){
                writer.println(s);
            }
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
     * Method used to load the file extension file
     */
    private ArrayList<String> loadFile(File file){
        ArrayList<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line = br.readLine();

            while(line != null){
                result.add(line);
                line = br.readLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Collections.sort(result);
        return result;
    }





    // ////////////////////////////////////
    // Private Methods to Search
    // ////////////////////////////////////

    private Set<Movie> searchCategory(String category, Set<Movie> list) {
        if(!categories.contains(category)) return list;

        Set<Movie> moviesResult = new HashSet<>();

        for(Movie m : list){
            for(String s : m.categories){
                if(s.equals(category)){
                    moviesResult.add(m);
                }
            }
        }

        return moviesResult;
    }

    private Set<Movie> searchString(String searchString, Set<Movie> list){
        String searchStringLow = searchString.toLowerCase();
        Set<Movie> moviesResult = new HashSet<>();

        for(Movie m : list){
            String titleLow = m.title.toLowerCase();
            if(titleLow.contains(searchStringLow)){
                moviesResult.add(m);
            } else{
                for(String cat : m.categories){
                    String catLow = cat.toLowerCase();
                    if(catLow.contains(searchStringLow)){
                        moviesResult.add(m);
                        break;
                    }
                }
            }
        }

        return moviesResult;
    }




    // ////////////////////////////////////
    // Print methods
    // ////////////////////////////////////

    /**
     * Prints all movies in a nice list
     */
    public void printAllMovies(){
        printMoviesFromList(new ArrayList<Movie>(movies),"All Movies");
    }

    /**
     * Print the movies given in the list parameter
     */
    public void printMoviesFromList(ArrayList<Movie> list, String listTitle){
        System.out.println("Movie List:   " + listTitle);
        for(int i = 1; i < list.size()+1; i++){
            Movie m = list.get(i-1);
            System.out.println("  " + i + ". " + m);
        }
    }

    public void printCategories(){
        System.out.print("Category list: " + categories.toString());
    }
}
