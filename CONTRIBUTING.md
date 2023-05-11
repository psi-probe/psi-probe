## Contributions ##

Please follow [GitHub Flow](https://guides.github.com/introduction/flow/), with the following additions:

*	**Ensure an [issue](//github.com/psi-probe/psi-probe/issues) exists** before you begin work. If not, file one.
	*	Your change should provide obvious material benefit to the project's users and/or developers. If in doubt, an issue serves as place to discuss your suggestion before you begin work.
	*	Issues power the release changelogs, so your change will be made known to users after it's done.
*	**One PR per issue.** Include the issue number in your commit(s) and PR so that merging it will close the issue.
*	**One issue per PR.** Keep changes minimal, and keep the scope narrow. A small change is easier to review.
	*	Avoid making formatting changes alongside functionality changes. This is a recipe for conflicts.
	*	Avoid bumping version numbers or correcting spelling errors along with your changes unless they're necessary.
	*	Feel free to make these sorts of corrections in a separate PR, though!
*   **Ensure commits are clean.** Please rebase before submitting PR. This will save time and ensure quicker merge. Use steps similar to following.
    *   git pull --rebase upstream master
	*   git push origin +master
