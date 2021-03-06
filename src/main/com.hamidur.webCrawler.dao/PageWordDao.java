package com.hamidur.webCrawler.dao;

import com.hamidur.webCrawler.models.Page;
import com.hamidur.webCrawler.models.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class PageWordDao
{
    private Connection connection;

    public PageWordDao(Connection connection)
    {
        this.connection = connection;
    }

    public void insertPageWords(Page page, Set<Word> wordsToInsert, Map<Word, Integer> wordsFrequency)
            throws SQLException
    {
        final String pageWordSql = "Insert into Pages_Words (pageId, wordId, frequency) values(?, ?, ?)";
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(pageWordSql))
        {
            for(Word word : wordsToInsert)
            {
                if(!isExists(page.getPageId(), word.getWordId()))
                {
                    int wordFrequency = wordsFrequency.get(word);
                    preparedStatement.setInt(1, page.getPageId());
                    preparedStatement.setInt(2, word.getWordId());
                    preparedStatement.setInt(3, wordFrequency);
                    preparedStatement.executeUpdate();
                }
            }
        }
    }

    public void insertPageWord(Page page, Map<Word, Integer> wordsIds, Map<Word, Integer> wordsFrequency)
            throws SQLException
    {
        final String pageWordSql = "Insert into Pages_Words (pageId, wordId, frequency) values(?, ?, ?)";
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(pageWordSql))
        {
            for(Word word : wordsIds.keySet())
            {
                int wordId = wordsIds.get(word);

                if(!isExists(page.getPageId(), wordId))
                {
                    int wordFrequency = wordsFrequency.get(word);
                    preparedStatement.setInt(1, page.getPageId());
                    preparedStatement.setInt(2, wordId);
                    preparedStatement.setInt(3, wordFrequency);
                    preparedStatement.executeUpdate();
                }
            }
        }
    }

    public boolean isExists(int pageId, int wordId) throws SQLException
    {
        final String sql = "Select pageId, wordId from Pages_Words where pageId=? and wordId=?";

        try(PreparedStatement preparedStatement = this.connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1, pageId);
            preparedStatement.setInt(2, wordId);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                return resultSet.next();
            }
        }
    }
}
