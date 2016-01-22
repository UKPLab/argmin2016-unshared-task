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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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
     * Crawls all URLs from the given list and stores them in the output folder
     *
     * @param urls      list of urls for Room for debate
     * @param outputDir output
     * @throws IOException ex
     */
    public static void crawlPages(List<String> urls, File outputDir)
            throws IOException
    {
        for (String url : urls) {
            NYTimesCommentsScraper nyTimesCommentsScraper = new NYTimesCommentsScraper();

            String html;
            try {
                html = nyTimesCommentsScraper.readHTML(url);
            }
            catch (InterruptedException e) {
                throw new IOException(e);
            }

            // file name
            File outFile = new File(outputDir, URLEncoder.encode(url, "utf-8") + ".html");

            FileUtils.writeStringToFile(outFile, html);
        }
    }

    public static void main(String[] args)
            throws Exception
    {
        String crawledPagesFolder = args[0];
        String outputFolder = args[1];

        // read links from text file
        InputStream urlsStream = NYTimesScraper.class.getClassLoader()
                .getResourceAsStream("urls.txt");
        List<String> urls = IOUtils.readLines(urlsStream);

        // download all
        //        crawlPages(urls, new File(crawledPagesFolder));

        Collection<File> files = FileUtils
                .listFiles(new File(crawledPagesFolder), null, false);

        System.out.println(files);

        for (File file : files) {
            //            extractDocumentAndComments(file, new File(outputFolder));
        }
    }
}
