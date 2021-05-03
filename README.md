# txtExport
word导出txt文件

 一、git 使用 积累
 
     git 取消提交（add）操作、未pull便push的bug、git取消修改，恢复版本 命令大全

   1、git 取消提交（add）操作：通过回退版本实现。
   
     1）查看版本号：
        
        git log

     2）执行回退命令：

        git reset --soft 6fc75f3291c661abdca154ac8d640977a8c97e4b

     3）更新最新版本：注意查看文件是否有误
     
        git pull origin master

     4）推到远程仓库
     
        git push origin

   2、Bug：未pull便push
   
      1） 在remote add后不要着急git add，一定要git pull origin master，否则会出现下面这个错误信息

            ! [rejected]        master -> master (non-fast-forward)
            error: failed to push some refs to 'https://github.com/wongArchitect/txtExport.git'

      2） 解决方法
      
            pull（拉取）下，再push（推）到远程仓库即可。
            
            注：当然，很有可能会产生冲突，可以试试清除缓存区，看着解决吧。


   3、git 取消修改，恢复版本 命令大全

        #取消对文件的修改。还原到最近的版本，废弃本地做的修改。
        git checkout -- <file>
        
        #取消已经暂存的文件。即，撤销先前"git add"的操作
        git reset HEAD <file>...
        
        #修改最后一次提交。用于修改上一次的提交信息，或漏提交文件等情况。
        git commit --amend
        
        #回退所有内容到上一个版本
        git reset HEAD^
        
        #回退a.py这个文件的版本到上一个版本  
        git reset HEAD^ a.py  
        
        #向前回退到第3个版本  
        git reset –soft HEAD~3  
        
        #将本地的状态回退到和远程的一样  
        git reset –hard origin/master  
        
        #回退到某个版本  
        git reset 057d  
        
        #回退到上一次提交的状态，按照某一次的commit完全反向的进行一次commit.(代码回滚到上个版本，并提交git)
        git revert HEAD
