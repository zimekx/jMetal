write("", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex",append=FALSE)
resultDirectory<-"/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/data"
latexHeader <- function() {
  write("\\documentclass{article}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\title{StandardStudy}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\usepackage{amssymb}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\begin{document}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\maketitle", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\section{Tables}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\caption{", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write(problem, "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write(".IGD.}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)

  write("\\label{Table:", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write(problem, "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write(".IGD.}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)

  write("\\centering", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\begin{scriptsize}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\begin{tabular}{", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write(tabularString, "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write(latexTableFirstLine, "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\hline ", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
}

printTableLine <- function(indicator, algorithm1, algorithm2, i, j, problem) { 
  file1<-paste(resultDirectory, algorithm1, sep="/")
  file1<-paste(file1, problem, sep="/")
  file1<-paste(file1, indicator, sep="/")
  data1<-scan(file1)
  file2<-paste(resultDirectory, algorithm2, sep="/")
  file2<-paste(file2, problem, sep="/")
  file2<-paste(file2, indicator, sep="/")
  data2<-scan(file2)
  if (i == j) {
    write("-- ", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  }
  else if (i < j) {
    if (is.finite(wilcox.test(data1, data2)$p.value) & wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) <= median(data2)) {
        write("$\\blacktriangle$", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
      }
      else {
        write("$\\triangledown$", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE) 
      }
    }
    else {
      write("--", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE) 
    }
  }
  else {
    write(" ", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  }
}

latexTableTail <- function() { 
  write("\\hline", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\end{tabular}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\end{scriptsize}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
  write("\\end{table}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
}

### START OF SCRIPT 
# Constants
problemList <-c("SmartLeveesSunny", "SmartLeveesCloudy") 
algorithmList <-c("NSGAII", "SPEA2") 
tabularString <-c("lc") 
latexTableFirstLine <-c("\\hline  & SPEA2\\\\ ") 
indicator<-"IGD"

 # Step 1.  Writes the latex header
latexHeader()
tabularString <-c("| l | p{0.15cm }p{0.15cm } | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} & \\multicolumn{2}{c|}{SPEA2} \\\\") 

# Step 3. Problem loop 
latexTableHeader("SmartLeveesSunny SmartLeveesCloudy ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "SPEA2") {
    write(i , "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
    write(" & ", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
          } 
          if (problem == "SmartLeveesCloudy") {
            if (j == "SPEA2") {
              write(" \\\\ ", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
            } 
            else {
              write(" & ", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
            }
          }
     else {
    write("&", "/Users/adamzima/semestr8/jmetal-5/jMetal/smart_levees/SmartLeveesStudy/R/IGD.Wilcoxon.tex", append=TRUE)
     }
        }
      }
      jndx = jndx + 1
    }
    indx = indx + 1
  }
} # for algorithm

  latexTableTail()

#Step 3. Writes the end of latex file 
latexTail()

