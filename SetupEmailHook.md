Insert the following into your 'hooks/post-commit' script

```
wget -o /dev/null -O /dev/null http://localhost:8080/subversionadmin/trigger/mail?revision=$REV
```

Where 'http://localhost:8080/subversionadmin/' is the base location of the deployed application