<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">

        <html lang="en-US">
            <head>
                <title>Test Summary</title>
                <meta charset="utf-8"/>
            </head>
            <body>
                
                <p>Test Summary</p>
                <p>Test start date: <xsl:value-of select="/testSummary/testDate/@startTime"/></p>
                <p>Test end date: <xsl:value-of select="/testSummary/testDate/@endTime"/></p>
                
                <table border="1" style="width:100%">
                    <tr>
                        <th>Test Configration</th>
                    </tr>   
                    
                    <tr>
                        <td>Config file:<xsl:value-of select="/testSummary/testConfiguration/testConfigurationFile"/></td>
                    </tr>
                    
                    <table border="1" style="width:100%">
                        <tr>
                            <th>Parameter</th>
                            <th>Value</th>
                        </tr>
                        
                        <xsl:for-each select="/testSummary/testConfiguration/parameters/parameter">
                            
                            <tr>
                                <td><xsl:value-of select="@name"/></td>
                                <td>
                                    <xsl:if test=".=''">...EMPTY...</xsl:if>
                                    <xsl:value-of select="."/>
                                </td>
                            </tr>
                            
                        </xsl:for-each>
                    </table>
                        
                </table>
                
                
                
                <xsl:for-each select="/testSummary/testResults/testSuite">
                    <br/>
                    <table border="1" style="width:100%">
                        <tr>
                            <th>Test Suite <xsl:value-of select="@name"/></th>
                        </tr>
                        <tr>
                            <th>Result: <xsl:value-of select="@result"/></th>
                        </tr>
                        <table border="1" style="width:100%">
                            <tr>
                                <th>Test Case</th>
                                <th>Result</th>
                            </tr>
                            <xsl:for-each select="testCase">
                                <tr>
                                    <td>
                                        <xsl:value-of select="@name"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="@result"/>
                                    </td>
                                </tr>
                            </xsl:for-each>
                        </table>
                    </table>
                </xsl:for-each>

            </body>
        </html>

    </xsl:template>
</xsl:stylesheet>
