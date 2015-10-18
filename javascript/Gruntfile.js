'use strict';

var vendor = [];

var source = [
    'app/*.js'
];

module.exports = function (grunt) {

    grunt.initConfig({

        pkg: grunt.file.readJSON('package.json'),

        sass: {

            dist: {
                files: [
                    {
                        src: ['app/app.sass'],
                        dest: '.',
                        expand: true,
                        ext: '.css'
                    }
                ]
            }

        },

        watch: {

            sass: {
                files: 'app/*.sass',
                tasks: ['sass']
            },

            livereload: {
                options: {
                    livereload: '<%= connect.options.livereload %>'
                },
                files: [
                    '*.html',
                    'app/*.css',
                    'app/*.js'
                ]
            }

        },

        connect: {

            options: {
                port: 9000,
                livereload: 35729,
                hostname: 'localhost'
            },

            livereload: {
                options: {
                    open: true,
                    base: [
                        '.'
                    ]
                }
            }

        },

        clean: {
            dist: ['dist']
        },

        copy: {
            dist: {
                files: [
                    {
                        expand: true,
                        src: [
                            '*.html',
                            'img/**/*'
                        ],
                        dest: 'dist/',
                        filter: 'isFile'
                    }
                ]
            }
        },

        cssmin: {
            target: {
                files: {
                    'dist/app/app.css': ['app/app.css']
                }
            }
        },

        uglify: {
            target: {
                files: {
                    'dist/app/app.js': source
                }
            }
        },

        concat: {
            options: {
                separator: ';'
            },
            target: {
                src: vendor,
                dest: 'dist/app/vendor.min.js'
            }
        },

        'gh-pages': {
            options: {
                base: 'dist'
            },
            src: '**/*'
        }

    });

    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-gh-pages');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');

    grunt.registerTask('default', ['sass', 'connect:livereload', 'watch']);
    grunt.registerTask('dist', ['sass', 'clean:dist', 'cssmin', 'uglify', 'concat', 'copy:dist']);
    grunt.registerTask('deploy', ['dist', 'gh-pages']);

};