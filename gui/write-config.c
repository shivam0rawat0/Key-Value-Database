#include <stdio.h>
#include <stdlib.h>

void write(char* dir, char * data){
	FILE *fptr;
	fptr = fopen(dir, "a");
	fprintf(fptr, data);
	fclose(fptr); 
}

char* concat(char** lines){
	int len = 0,i;
	for(i=0;lines[i]!=NULL;++i){
		int li = -1;
		while(lines[i][++li]!='\0');
		len+=li;
	}
	char * cat = (char*)malloc(sizeof(char)*len+1);
	int ci = -1;
	for(i=0;i<lines[i]!=NULL;++i){
		int li = -1;
		while(lines[i][++li]!='\0')
			cat[++ci] = lines[i][li];
	}
	cat[++ci]='\0';
	return cat;
}

int main( int argc, char *argv[] )
{
	FILE *fp;
	char path[32767];
	fp = popen("cmd.exe /c echo %cd%", "r");
	if (fp == NULL) {
		printf("failed to create pid ,log & config file\n" );
		exit(1);
	}
	fgets(path, sizeof(path), fp);
	int l = strlen(path);
	path[l-1] = '\\';
	printf("%s", path);
	pclose(fp);
	char * pidfile = concat((char*[]){path,"java.pid",NULL}); 
	write(pidfile,"");
	char * logfile = concat((char*[]){path,"file-db\\engine.log",NULL});
	write(logfile,"");
	char * configdata = concat((char*[]){
		"db.pid=",pidfile,
		"\ndb.path=",path,"file-db\\",
		"\ndb.log.dir=",logfile,
		"\ndb.log.type=read+write",
		"\ndb.name=sampleDB",
		"\ndb.active=false",
		"\ndb.raid.port=3040",
		"\ndb.raid.count=1",NULL});
	write(concat((char*[]){path,"file-db\\config.txt",NULL}),configdata);
	return 0;
}
