# add all untracked files to stage
git add .
# add specific file in dir
git add /path/to/dir
# add specific extension
git add *.txt

# Observe changes
git diff
git diff --staged
git diff --cached

# show status of woring dir
git status

# remove a file from the staging area
git reset
git reset <file_name>

# commit staged changes
git commit -m "message"
# edit the lastest commit message
git commit --ammend -m "ammended message"

# show logs
git log
git log --oneline
git log --graph --oneline --decorate
# most 3 recent logs
git log -3
# filter by date/ author
git log --after="2025/08/23"
git log --author="dngvmnh"

# hidding file that will not be pushed
git stash
git stash list
git stash pop
git stash drop
git stash apply
git stash apply stash@{0}

# push local repo to online repo
git push
# pull online repo to local repo
git pull

# list all branch
git branch
# create a new branch
git branch <new_branch>
# rename current branch
git branch -m <rename>
# force delete a branch ( use -d to delete branch with unmerged changes)
git branch -D <branch>

# move to another branch
git checkout <test_branch>
# create and move to another branch
git checkout -b <test_branch>

# merge branch named "test_branch" to the current branch
git merge test_branch

# cherry-picking, pick a commit from branch_b anf apply it to branch_a
git checkout branch_b
git log 
# find the commit and note the SHA
git checkout branch_a
git cherry-pick <commitSHA>

# git remove_password_file challenge
# get hash
git log --oneline 
git checkout <hash>
# create a new branch at the point the password was not leaked
git checkout -b remove_password_file
git reset --hard <hash>
git push -f
# replace the main branch with the remove_password_file
git branch -M remove_password_file main