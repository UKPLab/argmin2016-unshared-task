/*
 * Copyright 2016
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tudarmstadt.ukp.argumentation.data.roomfordebate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Main class for scraping data from Room for Debate
 *
 * @author Ivan Habernal
 */
public class NYTimesScraper
{

    /**
     * Crawls all URLs from the given list and stores them in the output folder; files that
     * already exist in the output folder are skipped
     *
     * @param urls      list of urls for Room for debate
     * @param outputDir output
     * @throws IOException ex
     */
    public static void crawlPages(List<String> urls, File outputDir)
            throws IOException
    {
        for (String url : urls) {
            // file name
            File outFile = new File(outputDir, URLEncoder.encode(url, "utf-8") + ".html");

            if (!outFile.exists()) {
                NYTimesCommentsScraper nyTimesCommentsScraper = new NYTimesCommentsScraper();

                String html;
                try {
                    html = nyTimesCommentsScraper.readHTML(url);
                }
                catch (InterruptedException e) {
                    throw new IOException(e);
                }

                FileUtils.writeStringToFile(outFile, html);
            }
        }
    }

    public static void main(String[] args)
            throws Exception
    {
        File crawledPagesFolder = new File(args[0]);
        if (!crawledPagesFolder.exists()) {
            crawledPagesFolder.mkdirs();
        }

        File outputFolder = new File(args[1]);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        // read links from text file
        final String urlsResourceName = "roomfordebate-urls.txt";

        InputStream urlsStream = NYTimesScraper.class.getClassLoader()
                .getResourceAsStream(urlsResourceName);

        if (urlsStream == null) {
            throw new IOException("Cannot find resource " + urlsResourceName + " on the classpath");
        }

        // read list of urls
        List<String> urls = new ArrayList<>();
        LineIterator iterator = IOUtils.lineIterator(urlsStream, "utf-8");
        while (iterator.hasNext()) {
            // ignore commented url (line starts with #)
            String line = iterator.nextLine();
            if (!line.startsWith("#") && !line.trim().isEmpty()) {
                urls.add(line.trim());
            }
        }

        // download all
        crawlPages(urls, crawledPagesFolder);

        Collection<File> files = FileUtils.listFiles(crawledPagesFolder, null, false);

        System.out.println(files);

        int idCounter = 0;

        for (File file : files) {
            //            extractDocumentAndComments(file, new File(outputFolder));
            NYTimesCommentsScraper commentsScraper = new NYTimesCommentsScraper();
            NYTimesArticleExtractor extractor = new NYTimesArticleExtractor();

            String html = FileUtils.readFileToString(file, "utf-8");


            //            System.out.println(comments);

            idCounter++;
            File outputFile = new File(outputFolder, String.format("Cx%03d.txt", idCounter));

            try {
                List<Comment> comments = commentsScraper.extractComments(html);
                Article article = extractor.extractArticle(html);

                saveArticleToText(article, outputFile);
                System.out.println("Saved to " + outputFile);
            }
            catch (IOException ex) {
                System.err.println(file.getName() + "\n" + ex.getMessage());
            }

        }
    }

    public static void saveArticleToText(Article article, File outputFile)
            throws IOException
    {
        PrintWriter pw = new PrintWriter(outputFile, "utf-8");

        pw.printf("Debate title: %s%n%nDebate description: %s%n%nArticle title: %s%n%n%s",
                article.getDebateTitle(),
                article.getDebateDescription(), article.getTitle(),
                StringUtils.join(article.getText().split("\n"), "\n\n"));

        IOUtils.closeQuietly(pw);
    }
}
