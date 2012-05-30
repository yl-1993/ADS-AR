/*
 * (C) Copyright 2005, Gregor Heinrich (gregor :: arbylon : net) (This file is
 * part of the lda-j (org.knowceans.lda.*) experimental software package.)
 */
/*
 * lda-j is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 */
/*
 * lda-j is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
/*
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on Dec 3, 2004
 * Modified by Dang Fan on May 29, 2012
 */
package org.knowceans.lda;

import java.io.*;
import java.util.Vector;

/**
 * Represents a corpus of documents.
 * <p>
 * lda-c reference: struct corpus in lda.h and function in lda-data.c.
 * 
 * @author heinrich
 */
public class Corpus {

    private Document[] docs;
    private int numTerms;
    private int numDocs;

    public Corpus(String dataFilename) {
        read(dataFilename);
    }

    static int OFFSET = 0; // offset for reading data

    /**
     * read a file in "pseudo-SVMlight" format.
     * irregular whitespace (duplicate spaces)
     * 
     * @param dataFilename
     */
    public void read(String dataFilename) {
        int length, count, word, n, nd, nw;

        System.out.println("reading data from " + dataFilename);

        try {
            Vector<Document> cdocs = new Vector<Document>();
            BufferedReader br = new BufferedReader(new FileReader(dataFilename));
            nd = 0;
            nw = 0;
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(" ");
                if (fields[0].equals("") || fields[0].equals(""))
                    continue;
                Document d = new Document();
                cdocs.add(d);
                length = Integer.parseInt(fields[0]);
                d.setLength(length);
                d.setTotal(0);
                d.setWords(new int[length]);
                d.setCounts(new int[length]);

                for (n = 0; n < length; n++) {
                    // fscanf(fileptr, "%10d:%10d", &word, &count);
                    String[] numbers = fields[n + 1].split(":");
                    if (numbers[0].equals("") || numbers[0].equals(""))
                        continue;
                    word = Integer.parseInt(numbers[0]);
                    count = (int) Float.parseFloat(numbers[1]);
                    word = word - OFFSET;
                    d.setWord(n, word);
                    d.setCount(n, count);
                    d.setTotal(d.getTotal() + count);
                    if (word >= nw) {
                        nw = word + 1;
                    }
                }

                nd++;
            }
            numDocs = nd;
            numTerms = nw;
            docs = (Document[]) cdocs.toArray(new Document[] {});
            System.out.println("number of docs    : " + nd);
            System.out.println("number of terms   : " + nw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getMaxCopusLength() {
    	int maxlen = 0;
    	for (Document d : docs) {
    		maxlen = maxlen > d.getLength() ? maxlen : d.getLength();
    	}
    	return maxlen;
    }

    public Document[] getDocs() {
        return docs;
    }

    public Document getDoc(int index) {
        return docs[index];
    }

    public void setDoc(int index, Document doc) {
        docs[index] = doc;
    }

    public int getNumDocs() {
        return numDocs;
    }

    public int getNumTerms() {
        return numTerms;
    }

    public void setDocs(Document[] documents) {
        docs = documents;
    }

    public void setNumDocs(int i) {
        numDocs = i;
    }

    public void setNumTerms(int i) {
        numTerms = i;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("Corpus {numDocs=" + numDocs + " numTerms=" + numTerms + "}");
        return b.toString();
    }
}
