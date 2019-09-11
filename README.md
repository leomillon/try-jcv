# Try-JCV

A Kotlin demo server-only packaged with JCV libraries.

## Manual installation :whale:

To create the docker image with the current pre-built wars:
```bash
$ docker build -t ekino/try-jcv .
```

Then run it:
```bash
$ docker run -p 8080:8080 ekino/try-jcv:latest
```

You can also use the docker-compose command:

```bash
$ docker-compose up
```

## Update the wars

Simply run the script:
```bash
$ ./build_war_files.sh
```

## Usage

Once the server has started, just add the server url (default is `http://localhost:8080`) to your Kotlin script `data-server` attribute.

Example:
```html
<pre>
<code class="kotlin-code" theme="darcula" data-target-platform="junit">
package com.ekino.oss.jcv.example.jcvcustomvalidatorexample

import com.ekino.oss.jcv.assertion.assertj.JsonCompareAssert.Companion.assertThatJson
import com.ekino.oss.jcv.core.JsonValueComparator
import com.ekino.oss.jcv.core.validator.Validators.defaultValidators
import com.ekino.oss.jcv.core.validator.validators
import org.junit.Test
import org.skyscreamer.jsonassert.ValueMatcherException

class Case1Test {

    @Test
    fun `should validate json content with custom validator`() {

        assertThatJson(
            &quot;&quot;&quot;
            {
              &quot;id&quot;: &quot;fda7a233-99b9-4756-8ecc-826a1c5a9bf5&quot;,
              &quot;reference&quot;: &quot;REF_0123456789&quot;
            }
            &quot;&quot;&quot;.trimIndent()
        )
            .using(validators {
                +defaultValidators()
                +templatedValidator(&quot;my_ref&quot;, MyRefComparator())
            })
            .isValidAgainst(
                &quot;&quot;&quot;
                {
                  &quot;id&quot;: &quot;{#uuid#}&quot;,
                  &quot;reference&quot;: &quot;{#my_ref#}&quot;
                }
                &quot;&quot;&quot;.trimIndent()
            )
    }

    private class MyRefComparator : JsonValueComparator&lt;String&gt; {

        override fun hasCorrectValue(actual: String?, expected: String?): Boolean {
            if (actual != null &amp;&amp; actual.startsWith(&quot;REF_&quot;) &amp;&amp; actual.length == 14) {
                return true
            }
            throw ValueMatcherException(&quot;Invalid reference format&quot;, expected, actual)
        }
    }
}
</code>
</pre>

<script src="https://unpkg.com/kotlin-playground@1"
        data-selector=".kotlin-code"
        data-server="http://localhost:8080">
</script>
```
