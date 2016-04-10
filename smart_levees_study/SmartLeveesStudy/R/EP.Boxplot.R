postscript("EP.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
resultDirectory<-"../data"
qIndicator <- function(indicator, problem)
{
fileNSGAII<-paste(resultDirectory, "NSGAII", sep="/")
fileNSGAII<-paste(fileNSGAII, problem, sep="/")
fileNSGAII<-paste(fileNSGAII, indicator, sep="/")
NSGAII<-scan(fileNSGAII)

fileSPEA2<-paste(resultDirectory, "SPEA2", sep="/")
fileSPEA2<-paste(fileSPEA2, problem, sep="/")
fileSPEA2<-paste(fileSPEA2, indicator, sep="/")
SPEA2<-scan(fileSPEA2)

algs<-c("NSGAII","SPEA2")
boxplot(NSGAII,SPEA2,names=algs, notch = FALSE)
titulo <-paste(indicator, problem, sep=":")
title(main=titulo)
}
par(mfrow=c(3,3))
indicator<-"EP"
qIndicator(indicator, "SmartLeveesSunny")
qIndicator(indicator, "SmartLeveesCloudy")
