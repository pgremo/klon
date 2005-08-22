<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:lxslt="http://xml.apache.org/xslt" xmlns:stringutils="xalan://org.apache.tools.ant.util.StringUtils">

<xsl:output method="html" encoding="ISO-8859-1" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>

<xsl:decimal-format decimal-separator="." grouping-separator="," />

<xsl:template match="testsuites">
    <html>
        <head>
	        <title>Unit Test Results</title>
					<link  rel="StyleSheet" type="text/css" href="maven-base.css" />
					<link  rel="StyleSheet" type="text/css" href="maven-theme.css" />
					<link  rel="StyleSheet" type="text/css" href="project.css" />
        </head>
				<body class="composite">
						<div id="bodyColumn">
							<div class="contentBox">
								<div class="section">
									<div class="sectionTitle"><a name="Title">Unit Test Results</a></div>
									<p>
										[<a href="#Summary">summary</a>] [<a href="#Package_List">package list</a>] [<a href="#Test_Cases">test cases</a>]
									</p>
									<p>Designed for use with <a href='http://www.junit.org'>JUnit</a> and <a href='http://ant.apache.org/ant'>Ant</a>.</p>
								</div>				
							
								<!-- Summary part -->
								<xsl:call-template name="summary"/>
								
								<!-- Package List part -->
								<xsl:call-template name="packagelist"/>
								
								<!-- For each package create its part -->
								<xsl:call-template name="packages"/>
								
								<!-- For each class create the  part -->
								<xsl:call-template name="classes"/>
							</div>
					</div>
		    </body>
    </html>
</xsl:template>
    
<xsl:template name="packagelist">   
	<div class="section">
		<div class="sectionTitle"><a name="Package_List">Packages</a></div>
		<p>
			[<a href="#Summary">summary</a>] [<a href="#Package_List">package list</a>] [<a href="#Test_Cases">test cases</a>]
		</p>
	    <table class="bodyTable">
		    <tr>
		        <th>Name</th>
		        <th>Tests</th>
		        <th>Errors</th>
		        <th>Failures</th>
		        <th>Time</th>
		    </tr>
	        <xsl:for-each select="./testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
	            <xsl:sort select="@package"/>
	            <xsl:variable name="testsuites-in-package" select="/testsuites/testsuite[./@package = current()/@package]"/>
	            <xsl:variable name="testCount" select="sum($testsuites-in-package/@tests)"/>
	            <xsl:variable name="errorCount" select="sum($testsuites-in-package/@errors)"/>
	            <xsl:variable name="failureCount" select="sum($testsuites-in-package/@failures)"/>
	            <xsl:variable name="timeCount" select="sum($testsuites-in-package/@time)"/>
	            <tr>
					<xsl:call-template name="alternated-row"/>
	                <td><a href="#{@package}"><xsl:value-of select="@package"/></a></td>
	                <td><xsl:value-of select="$testCount"/></td>
	                <td><xsl:value-of select="$errorCount"/></td>
	                <td><xsl:value-of select="$failureCount"/></td>
			        <td><xsl:value-of select="format-number($timeCount,'0.000')"/></td>
	            </tr>
	        </xsl:for-each>
	    </table>    
    Note: package statistics are not computed recursively, they only sum up all of its testsuites numbers.
    </div>    
</xsl:template>
    
<xsl:template name="packages">
    <xsl:for-each select="/testsuites/testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
        <xsl:sort select="@package"/>
            <div class="section">
							<div class="sectionTitle"><a name="{@package}"><xsl:value-of select="@package"/></a></div>
	            <table class="bodyTable">
				    <tr>
				        <th>Name</th>
				        <th>Tests</th>
				        <th>Errors</th>
				        <th>Failures</th>
				        <th>Time</th>
				    </tr>
	                <xsl:apply-templates select="/testsuites/testsuite[./@package = current()/@package]" mode="print.test"/>
	            </table>
            </div>
    </xsl:for-each>
</xsl:template>
    
<xsl:template name="classes">
    <div class="section">
		<div class="sectionTitle"><a name="Test_Cases">Test Cases</a></div>
		<p>
			[<a href="#Summary">summary</a>] [<a href="#Package_List">package list</a>] [<a href="#Test_Cases">test cases</a>]
		</p>
	    <xsl:for-each select="testsuite">
	        <xsl:sort select="@name"/>
						<div class="sectionTitle"><a name="{@name}"><xsl:value-of select="@name"/></a></div>
		        <table class="bodyTable">
					<xsl:apply-templates select="./testcase" mode="print.test"/>
		        </table>
	    </xsl:for-each>
    </div>
</xsl:template>
    
<xsl:template name="summary">
	<div class="section">
		<div class="sectionTitle"><a name="Summary">Summary</a></div>
		<p>
			[<a href="#Summary">summary</a>] [<a href="#Package_List">package list</a>] [<a href="#Test_Cases">test cases</a>]
		</p>
		<table class="bodyTable">
		    <tr>
		        <th>Tests</th>
		        <th>Failures</th>
		        <th>Errors</th>
		        <th>Success rate</th>
		        <th>Time</th>
		    </tr>
		    <tr>
				<xsl:call-template name="alternated-row"/>
			    <xsl:variable name="testCount" select="sum(testsuite/@tests)"/>
			    <xsl:variable name="errorCount" select="sum(testsuite/@errors)"/>
			    <xsl:variable name="failureCount" select="sum(testsuite/@failures)"/>
			    <xsl:variable name="timeCount" select="sum(testsuite/@time)"/>
			    <xsl:variable name="successRate" select="($testCount - $failureCount - $errorCount) div $testCount"/>
		        <td><xsl:value-of select="$testCount"/></td>
		        <td><xsl:value-of select="$failureCount"/></td>
		        <td><xsl:value-of select="$errorCount"/></td>
		        <td><xsl:value-of select="format-number($successRate,'0.00%')"/></td>
		        <td><xsl:value-of select="format-number($timeCount,'0.000')"/></td>
		    </tr>
	    </table>
Note: <i>failures</i> are anticipated and checked for with assertions while <i>errors</i> are unanticipated.
	</div>
</xsl:template>
    
<xsl:template match="testsuite" mode="print.test">
    <tr valign="top">
		<xsl:call-template name="alternated-row"/>
        <td><a href="#{@name}"><xsl:value-of select="@name"/></a></td>
        <td><xsl:value-of select="@tests"/></td>
        <td><xsl:value-of select="@errors"/></td>
        <td><xsl:value-of select="@failures"/></td>
        <td><xsl:value-of select="format-number(@time,'0.000')"/></td>
    </tr>
</xsl:template>

<xsl:template match="testcase" mode="print.test">
    <tr>
				<xsl:call-template name="alternated-row"/>
        <xsl:choose>
            <xsl:when test="failure">
                <td>Failure</td>
            </xsl:when>
            <xsl:when test="error">
                <td>Error</td>
            </xsl:when>
            <xsl:otherwise>
                <td>Success</td>
            </xsl:otherwise>
        </xsl:choose>
        <td width="80%"><xsl:value-of select="@name"/></td>
        <td><xsl:value-of select="format-number(@time,'0.000')"/></td>
    </tr>
		<xsl:if test="failure or error">
	    <tr>
				<xsl:call-template name="alternated-row"/>
	    	<td colspan="3">
					<pre><xsl:value-of select="*//text()" /></pre>
				</td>
	    </tr>
    </xsl:if>
</xsl:template>

<xsl:template name="alternated-row">
	<xsl:attribute name="class">
		<xsl:if test="position() mod 2 = 1">a</xsl:if>
		<xsl:if test="position() mod 2 = 0">b</xsl:if>
	</xsl:attribute>  
</xsl:template>	

</xsl:stylesheet>

