<xsl:stylesheet	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="html" encoding="ISO-8859-1" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>
	
<xsl:decimal-format decimal-separator="." grouping-separator="," />

<xsl:param name="src"></xsl:param>

<xsl:template match="checkstyle">
	<html>
		<head>
        <title>Checkstyle Audit</title>
				<link  rel="StyleSheet" type="text/css" href="maven-base.css" />
				<link  rel="StyleSheet" type="text/css" href="maven-theme.css" />
				<link  rel="StyleSheet" type="text/css" href="project.css" />
		</head>
			<body class="composite">
				<div id="bodyColumn">
					<div class="contentBox">
						<div class="section">
							<div class="sectionTitle"><a name="Coverage_Report">Checkstyle Results</a></div>
								<p>Designed for use with <a href='http://checkstyle.sourceforge.net/'>CheckStyle</a> and <a href='http://ant.apache.org'>Ant</a>.</p>
						</div>
						<!-- Summary part -->
						<xsl:apply-templates select="." mode="summary"/>
							
						<!-- Package List part -->
						<xsl:apply-templates select="." mode="filelist"/>
							
						<!-- For each package create its part -->
						<xsl:for-each select="file">
							<xsl:sort select="@name"  case-order="upper-first"/>
							<xsl:apply-templates select="."/>
						</xsl:for-each>
					</div>
				</div>
			</body>
	</html>
</xsl:template>
	
<xsl:template match="checkstyle" mode="filelist">	
	<xsl:variable name="errorCount" select="count(file/error)"/>
	<xsl:if test="$errorCount != 0">
	<div class="section">
		<div class="sectionTitle"><a name="Files">Files</a></div>
		<table class="bodyTable">
			<tr>
				<th>Name</th>
				<th>I</th>
				<th>W</th>
				<th>E</th>
			</tr>
			<xsl:for-each select="file">
				<xsl:sort select="@name"  case-order="upper-first"/>
				<xsl:variable name="infoCount" select="count(error[@severity='info'])"/>				
				<xsl:variable name="warningCount" select="count(error[@severity='warning'])"/>				
				<xsl:variable name="errorCount" select="count(error[@severity='error'])"/>				
				<xsl:if test="$infoCount + $warningCount + $errorCount != 0">
				<tr>
					<xsl:call-template name="alternated-row"/>
					<td><a href="#f-{@name}"><xsl:value-of select="@name"/></a></td>
					<td><xsl:value-of select="$infoCount"/></td>
					<td><xsl:value-of select="$warningCount"/></td>
					<td><xsl:value-of select="$errorCount"/></td>
				</tr>
				</xsl:if>
			</xsl:for-each>
		</table>		
	</div>
	</xsl:if>
</xsl:template>

<xsl:template match="file">
	<xsl:variable name="errorCount" select="count(error)"/>
	<xsl:if test="$errorCount != 0">
		<div class="section">
		<a name="f-{@name}"></a>
		<div class="sectionTitle"><xsl:value-of select="substring(substring-after(@name, $src), 2)"/></div>
		<table class="bodyTable">
			<tr>
				<th colspan="2">Description</th>
				<th>Line</th>
			</tr>
			<xsl:for-each select="error">
			<tr>
				<xsl:call-template name="alternated-row"/>
				<td>
					<xsl:choose>
						<xsl:when test="@severity='info'">
							I
						</xsl:when>
						<xsl:when test="@severity='warning'">
							W
						</xsl:when>
						<xsl:when test="@severity='error'">
							E
						</xsl:when>
					</xsl:choose>
				</td>
				<td><xsl:value-of select="@message"/></td>
				<td><xsl:value-of select="@line"/></td>
			</tr>
			</xsl:for-each>
		</table>
		</div>
	</xsl:if>
</xsl:template>

<xsl:template match="checkstyle" mode="summary">
	<div class="section">
		<div class="sectionTitle"><a name="Summary">Summary</a></div>
		<table class="bodyTable" >
		<thead>
			<tr class="a">
				<th>Files</th>
				<th style="width:75px">Info</th>
				<th style="width:75px">Warnings</th>
				<th style="width:75px">Errors</th>
			</tr>
		</thead>
		<tbody>
			<tr>
			  <xsl:variable name="fileCount" select="count(file/error[1])"/>
			  <xsl:variable name="infoCount" select="count(file/error[@severity='info'])"/>
			  <xsl:variable name="warningCount" select="count(file/error[@severity='warning'])"/>
			  <xsl:variable name="errorCount" select="count(file/error[@severity='error'])"/>
			  <xsl:call-template name="alternated-row"/>
				<td><xsl:value-of select="$fileCount"/></td>
				<td><xsl:value-of select="$infoCount"/></td>
				<td><xsl:value-of select="$warningCount"/></td>
				<td><xsl:value-of select="$errorCount"/></td>
			</tr>
		</tbody>
		</table>
	</div>
</xsl:template>

<xsl:template name="alternated-row">
	<xsl:attribute name="class">
		<xsl:if test="position() mod 2 = 1">a</xsl:if>
		<xsl:if test="position() mod 2 = 0">b</xsl:if>
	</xsl:attribute>  
</xsl:template>	

</xsl:stylesheet>


